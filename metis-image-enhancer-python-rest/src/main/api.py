import time
import logging
import socket
import os
import filetype
import uuid
import subprocess
import io

from flask import Flask, request, jsonify, send_file, make_response
from waitress import serve
from error_handlers import errors
from PIL import Image


if __name__=="__main__":
    app = Flask(__name__)
    app.register_blueprint(errors)
    host = '0.0.0.0'
    port = 8080
    app.logger.setLevel(logging.INFO)

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
            seed_file = str(uuid.uuid4())
            input_file = "/tmp/"+seed_file+".img"
            output_file = "/tmp/"+seed_file+"_out.img"
            app.logger.info('temporary files created')
            with open(input_file,"wb") as in_fp:
                in_fp.write(image_data)
            command = "python3 mie.py --input '{0}' --output '{1}'".format(input_file, output_file)
            subprocess.check_output([command], shell=True)

            # save it to memory in the original format
            sr_img = Image.open(output_file)
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

            os.remove(input_file)
            os.remove(output_file)
            app.logger.info('temporary files removed')
            # processing elapsed time
            end_process = time.time()
            response.headers['Elapsed-Time'] = end_process - start_process

            del raw_bytes
            del image_data

            return response, 200

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
