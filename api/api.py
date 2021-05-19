# -*- coding: utf-8 -*-
"""
Created on Mon Apr 19 13:08:18 2021

@author: Kunal
"""
from flask import Flask, request, jsonify
import joblib
import traceback
import pandas as pd
import sys
import numpy as np

# Your API definition
app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    if lr:
        try:
            json_ = request.json
            print(json_,file=sys.stderr)
            query = pd.DataFrame(json_)
            query = query.reindex(columns=model_columns, fill_value=0)
            query = query.iloc[:,:].values
            query = np.array(ct.transform(query))
            print(query,file=sys.stdout)
            prediction = list(lr.predict_proba(query))
            return jsonify({'prediction': str(prediction)})
        except:
            return jsonify({'trace': traceback.format_exc()})
    else:
        print ('Train the model first')
        return ('No model here to use')

if __name__ == '__main__':
    try:
        port = int(sys.argv[1]) # This is for a command-line input
    except:
        port = 12345 # If you don't provide any port the port will be set to 12345

    lr = joblib.load("model.pkl") # Load "model.pkl"
    print ('Model loaded')
    model_columns = joblib.load("model_columns.pkl") # Load "model_columns.pkl"
    print ('Model columns loaded')
    ct = joblib.load("transformer.pkl")
    print ('Column Transformer loaded')
    app.run(port=port, debug=True)
