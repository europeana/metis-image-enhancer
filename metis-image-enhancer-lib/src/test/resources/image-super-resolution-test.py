import numpy as np
import datetime
from PIL import Image
from ISR.models import RDN, RRDN

print("Starting")

#Load model
print("Start Load")
print(datetime.datetime.now())
model = RDN(weights='noise-cancel')
print("End Load")
print(datetime.datetime.now())

# # convert image to RGB format with white background
img = Image.open('src/test/resources/img/thumbnail.jpg').convert("RGBA")

print("Start Convert")
print(datetime.datetime.now())
# img = Image.open(image_file.stream).convert("RGBA")
rgb_img = Image.new("RGBA", img.size, "WHITE")
rgb_img.paste(img, mask=img)
sr_img = model.predict(np.array(rgb_img.convert("RGB")))
sr_img = Image.fromarray(sr_img)
print("End Convert")
print(datetime.datetime.now())

# Save image to file
sr_img.save('src/test/resources/img/thumbnail_enhanced.jpg')
print("End")