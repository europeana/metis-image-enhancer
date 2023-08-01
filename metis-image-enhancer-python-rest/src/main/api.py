import numpy as np
import time
import logging
import socket
import io
import filetype
import uuid
import os

from flask import Flask, request, jsonify, send_file
from waitress import serve
from error_handlers import errors
from PIL import Image
from ISR.models import RDN, RRDN


def write_temp_file(content, filename=None, mode='wb'):
    """Write content to a temporary file.

    If passing binary data the mode needs to be set to 'wb'.

    Args:
        content (bytes|str): The file content.
        filename (str, optional): The filename to use when writing the file. Defaults to None.
        mode (str, optional): The write mode ('w' or 'wb'). Defaults to w.

    Returns:
        str: Fully qualified path name for the file.
    """
    if filename is None:
        filename = str(uuid.uuid4())
    fqpn = os.path.join('/tmp', filename)
    os.makedirs(os.path.dirname(fqpn), exist_ok=True)
    with open(fqpn, mode) as fh:
        fh.write(content)
    return fqpn


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
            #image_file = request.files['image']
            byte_file = request.data
            image_file = Image.open(write_temp_file(byte_file))
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
