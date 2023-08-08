import numpy as np
import sys
import os
import getopt
import time
import filetype

from PIL import Image
from ISR.models import RDN

workingdirectory = os.getcwd()

# working directory
print('working dir:', workingdirectory)

# Arguments
def parameters(argv):
    arg_help = "{0} -i <input> -o <output>".format(argv[0])
    params = dict();

    try:
        opts, args = getopt.getopt(argv[1:], "hio:v", ["help", "input=", "output="])
    except:
        print(arg_help)
        sys.exit(2)

    if len(opts) == 0:
        print(arg_help)
    else:
        for opt, arg in opts:
            if opt in ("-h", "--help"):
                print(arg_help)  # print the help message
                sys.exit(2)
            elif opt in ("-i", "--input"):
                params['arg_input'] = arg

            elif opt in ("-o", "--output"):
                params[ 'arg_output'] = arg

        print('args:')
        for elem in map(str,params):
            print(elem+":"+params[elem], sep="\n")

    return params
def enhance(input_file, output_file, model):
    # Read the image from the file
    # convert image to RGB format with white background
    small_image = Image.open(input_file).convert("RGBA")
    rgb_img = Image.new("RGBA", small_image.size, "WHITE")
    rgb_img.paste(small_image, mask=small_image)

    # Enhance the image
    enhanced_image = model.predict(np.array(rgb_img.convert("RGB")))
    # find image format
    kind = filetype.guess(input_file)
    description = ""
    if kind is None or kind.extension not in ['jpg', 'bmp', 'gif', 'tif', 'png', 'apng', 'ico']:
        if kind is None:
            description = 'file type not supported!'
        else:
            description = 'file type '+kind.extension+' not supported!'

    if description != "":
        print(description)
        return 1

    if kind.extension == 'jpg':
        img_format = "jpeg"
    elif kind.extension == 'tif':
        img_format = "tiff"
    elif kind.extension == 'apng':
        img_format = "png"
    else:
        img_format = kind.extension
    # Write the image to the output file.
    Image.fromarray(enhanced_image).save(output_file, img_format)
    return 0
def main(params):
    # Load the prediction model. Can be initialized once and then reused?
    start = time.time()
    model = RDN(weights='noise-cancel')
    end = time.time()

    # elapsed time
    print ('loading model elapsed time: ',end - start)
    # Declare the input and output file locations
    inputFile = r'{0}'.format(params['arg_input']).strip()
    outputFile = r'{0}'.format(params['arg_output']).strip()
    enhance(inputFile, outputFile, model)

if __name__ == "__main__":
    main(parameters(sys.argv))
