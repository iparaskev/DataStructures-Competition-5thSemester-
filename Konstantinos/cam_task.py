import cv2
import time

camera_port = 0

camera = cv2.VideoCapture(camera_port)


# Capturing and saving
def image_cap(j):
    ret, im = camera.read()
    fname = "C:\Users\konstantinos\Desktop\image" + str(j) + ".png"
    cv2.imwrite(fname, im)
    return

# Taking 4 photos
for i in range(4):
    print ("Get ready...")
    time.sleep(3)  # 3 seconds waiting time
    image_cap(i)


camera.release()
