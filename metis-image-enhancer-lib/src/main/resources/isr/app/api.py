import numpy as np
import time
import logging
import socket
import io
import filetype

from flask import Flask, request, jsonify, send_file
from waitress import serve
from error_handlers import errors
from PIL import Image
from ISR.models import RDN, RRDN

if __name__=="__main__":
    app = Flask(__name__)
    app.register_blueprint(errors)
    host = '0.0.0.0'
    port = 5050
    app.logger.setLevel(logging.INFO)

    # Load the prediction model. Can be initialized once and then reused?
    start = time.time()
    model = RDN(weights='noise-cancel')
    end = time.time()

    # modal elapsed time
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
            app.logger.info('POST request running on host: ' + socket.gethostname())
            image_file = request.files['image']

            # find image format
            kind = filetype.guess(image_file.stream)
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
            img = Image.open(image_file.stream).convert("RGBA")
            rgb_img = Image.new("RGBA", img.size, "WHITE")
            rgb_img.paste(img, mask=img)
            sr_img = model.predict(np.array(rgb_img.convert("RGB")))
            sr_img = Image.fromarray(sr_img)

            # save it to memory in the original format
            raw_bytes = io.BytesIO()
            if kind.extension == 'jpg':
                format = "jpeg"
            elif kind.extension == 'tif':
                format = "tiff"
            elif kind.extension == 'apng':
                format = "png"
            else:
                format = kind.extension
            sr_img.save(raw_bytes, format)
            raw_bytes.seek(0)

            #response file with bytes and mime type
            return send_file(raw_bytes, mimetype=kind.mime)

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
