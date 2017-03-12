import numpy as np
import pandas as pd
np.random.seed(1337)  # for reproducibility

from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Flatten
from keras.layers import Convolution2D, MaxPooling2D
from keras.utils import np_utils
from keras.preprocessing import image
import tensorflow as tf
from keras.datasets import mnist
import matplotlib.pyplot as plt
import cv2
from keras.callbacks import ModelCheckpoint
import os
import sys
from PIL import Image, ImageOps
import pickle

from keras import backend as K

batch_size = 32
nb_classes = 10
nb_epoch = 120

img_rows, img_cols = 250,250
# number of convolutional filters to use
nb_filters = 32
# size of pooling area for max pooling
pool_size = (2, 2)
# convolution kernel size
kernel_size = (3, 3)




data = []
labels = []

labels_text = []

id_label = -1

rootdir = "images_top/"

# Get data
for folder, subs, files in os.walk(rootdir):
    with open(os.path.join(folder, 'python-outfile.txt'), 'w') as dest:
       labels_text.append((id_label, folder.split("/")[1]))
       for filename in files:
            if filename.endswith('.jpg'):
                try:
                    im = Image.open(folder + "/" + filename)
                    thumb = ImageOps.fit(im, (img_rows,img_cols), Image.ANTIALIAS)
                    aux = image.img_to_array(thumb)/ 255.0
                    if np.shape(aux) == (img_rows, img_cols,3):
                        data.append(aux)
                        labels.append(id_label)
                    else:
                        print (np.shape(aux), folder)

                except IOError as e:
                    print e
                    pass
                
                #print (rootdir, folder + "/" + filename, id_label)
    id_label += 1
    
data = np.array(data)
labels = np_utils.to_categorical(labels, id_label)
labels_text = labels_text[1:]
labels_text = np.array(labels_text)
out = dict()
for e in labels_text:
    out[e[0]] = e[1]
labels_text = out


'''
ind = np.random.choice(len(data),int(len(data)*0.8), replace=False)
X_train = data[ind]
Y_train = labels[ind]
X_val = data[~ind]
Y_val = data[~ind]
'''



'''
model = Sequential()

model.add(Convolution2D(32, 7,7,
                        border_mode='valid',
                        input_shape=(img_rows, img_cols, 3)))
model.add(Activation('relu'))
model.add(Convolution2D(32, 5,5))
#model.add(Dropout(0.25))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=pool_size))
model.add(Convolution2D(32, 3,3))
model.add(Dropout(0.25))

model.add(Flatten())
model.add(Dense(256))
model.add(Activation('relu'))
model.add(Dropout(0.5))
model.add(Dense(id_label))
model.add(Activation('softmax'))
'''

model = Sequential()
model.add(Convolution2D(32, 5, 5, input_shape=(img_rows, img_cols, 3)))
model.add(Activation('relu'))

model.add(Convolution2D(32, 3, 3))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Dropout(0.25))

model.add(Convolution2D(64, 3, 3))
model.add(Activation('relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Dropout(0.25))

model.add(Flatten())  # this converts our 3D feature maps to 1D feature vectors
model.add(Dense(128))
model.add(Activation('relu'))
model.add(Dropout(0.5))
model.add(Dense(56))
model.add(Activation('relu'))
model.add(Dense(id_label))
model.add(Activation('softmax'))


filepath="model/weights-improvement-{epoch:02d}-{val_acc:.2f}.hdf5"
checkpoint = ModelCheckpoint(filepath, monitor='val_acc', verbose=1, save_best_only=True, mode='max')
callbacks_list = [checkpoint]

model.compile(loss='categorical_crossentropy',
              optimizer='adam',
              metrics=['accuracy'])

#model.fit(X_train, Y_train, batch_size=batch_size, nb_epoch=nb_epoch,
 #         verbose=1, validation_data=(X_test, Y_test), callbacks=callbacks_list)


model.fit(data, labels, batch_size=16, nb_epoch=nb_epoch,
          validation_split=0.2, shuffle=True,
         verbose=1, callbacks=callbacks_list)


model.save('total_top.h5')

#model.fit(data, labels, callbacks=callbacks_list)

score = model.evaluate(data, labels, verbose=0)
print score


    
rootdir = "img_test/"
data_test = []
data_names = []
# Get data
for folder, subs, files in os.walk(rootdir):
    with open(os.path.join(folder, 'python-outfile.txt'), 'w') as dest:
       for filename in files:
         print (filename)
         if filename.endswith('.jpeg') or filename.endswith('.jpg'):
                try:
                    im = Image.open(folder + "/" + filename)
                    thumb = ImageOps.fit(im, (img_rows,img_cols), Image.ANTIALIAS)
                    aux = image.img_to_array(thumb)/ 255.0
                    if np.shape(aux) == (img_rows, img_cols,3):
                        data_test.append(aux)
                        data_names.append(filename)
                        print (np.shape(aux), folder)

                except IOError as e:
                    print e
                    pass
                
                #print (rootdir, folder + "/" + filename, id_label)
    id_label += 1

data_test = np.array(data_test)

pred = model.predict(data_test)

pred = np.argmax(pred, axis=1)

zip(data_names, pred, [labels_text[str(p)] for p in pred])



