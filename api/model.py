# -*- coding: utf-8 -*-
"""
Created on Mon Apr 19 14:45:06 2021

@author: Kunal
"""

# Naive Bayes

# Importing the libraries
import numpy as np
#import matplotlib.pyplot as plt
import pandas as pd

# Importing the dataset
dataset = pd.read_csv('iwander.csv')
model_columns = list(dataset.columns)
model_columns = model_columns[:-1]
X = dataset.iloc[:, :-1].values
y = dataset.iloc[:, -1].values

#preprocessing
# Encoding categorical data
# Encoding the Independent Variable
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder
ct = ColumnTransformer(transformers=[('encoder', OneHotEncoder(), [0,2])], remainder='passthrough')
X = np.array(ct.fit_transform(X))
#print(X)

from sklearn.preprocessing import LabelEncoder
le = LabelEncoder()
y = le.fit_transform(y)
#print(y)

# Splitting the dataset into the Training set and Test set
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.25, random_state = 0)
# print(X_train)
# print(y_train)
# print(X_test)
# print(y_test)

# Feature Scaling
# from sklearn.preprocessing import StandardScaler
# sc = StandardScaler()
# X_train = sc.fit_transform(X_train)
# X_test = sc.transform(X_test)
# print(X_train)
# print(X_test)

# Training the Naive Bayes model on the Training set
from sklearn.naive_bayes import GaussianNB
classifier = GaussianNB()
classifier.fit(X, y)
import joblib
joblib.dump(classifier, 'model.pkl')
joblib.dump(model_columns, 'model_columns.pkl')

# Predicting a new result
#print(classifier.predict(sc.transform([[30,87000]])))

# Predicting the Test set results
# y_pred = classifier.predict_proba(X_test)
# print(y_pred)
#print(np.concatenate((y_pred.reshape(len(y_pred),1), y_test.reshape(len(y_test),1)),1))
# Making the Confusion Matrix
# from sklearn.metrics import confusion_matrix, accuracy_score
# cm = confusion_matrix(y_test, y_pred)
# print(cm)
#accuracy_score(y_test, y_pred)