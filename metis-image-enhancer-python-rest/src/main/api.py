import numpy as np
import time
import logging
import socket
import io
import filetype
import gc

from flask import Flask, request, jsonify, send_file, make_response
from waitress import serve
from error_handlers import errors
from PIL import Image
from ISR.models import RDN

if __name__=="__main__":
    app = Flask(__name__)
    app.register_blueprint(errors)
    host = '0.0.0.0'
    port = 8080
    app.logger.setLevel(logging.INFO)

    # Load the prediction model. Can be initialized once and then reused?
    start = time.time()
    model = RDN(weights='noise-cancel')
    end = time.time()

    # model elapsed time
    app.logger.info('loading model elapsed time: %ds', end - start)

    @app.route("/")
    def index():
        """Return JSON with welcome message."""
        app.logger.info('GET request running on host: ' + socket.gethostname())
        response = {
            "code": 200,
            "name": "GET",
            "description": "Welcome to image super resolution enhancement"
        }
        return jsonify(response)

    @app.route('/enhance/image', methods=['POST','GET'])
    def enhance_image():
        """Return the content-type image enhanced."""
        if request.method == 'POST':
            start_process = time.time()
            app.logger.info('POST request running on host: ' + socket.gethostname())
            image_data = request.data

            # find image format
            kind = filetype.guess(image_data)
            if kind is None or kind.extension not in ['jpg', 'bmp', 'gif', 'tif', 'png', 'apng', 'ico']:
                if kind is None:
                    description = 'file type not supported!'
                else:
                    description = 'file type '+kind.extension+' not supported!'
                response = {
                    "code": 400,
                    "name": "POST",
                    "description": description
                }
                app.logger.warning(description)
                return jsonify(response)
            # convert image to RGB format with white background
            img = Image.open(io.BytesIO(image_data)).convert("RGBA")
            rgb_img = Image.new("RGBA", img.size, "WHITE")
            rgb_img.paste(img, mask=img)
            sr_img = model.predict(np.array(rgb_img.convert("RGB")), by_patch_of_size=16)
            sr_img = Image.fromarray(sr_img)

            # save it to memory in the original format
            raw_bytes = io.BytesIO()
            if kind.extension == 'jpg':
                img_format = "jpeg"
            elif kind.extension == 'tif':
                img_format = "tiff"
            elif kind.extension == 'apng':
                img_format = "png"
            else:
                img_format = kind.extension
            sr_img.save(raw_bytes, img_format)
            raw_bytes.seek(0)

            #response file with bytes and mime type
            response = make_response(send_file(raw_bytes, mimetype=kind.mime))
            # processing elapsed time
            end_process = time.time()
            response.headers['Elapsed-Time'] = end_process - start_process

            del raw_bytes
            del sr_img
            del rgb_img
            del img
            del image_data
            gc.collect()
            return response

        if request.method == 'GET':
            app.logger.info('GET request running on host: ' + socket.gethostname())
            response = {
                "code": 200,
                "name": "GET",
                "description": "Post here a low-resolution image in binary format"
            }
            return jsonify(response)

    # serve the application
    serve(app, host=host, port=port)
