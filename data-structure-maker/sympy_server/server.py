from flask import Flask, url_for, jsonify, json, request
import sympy

app = Flask(__name__)

@app.route('/variables', methods=["POST"])
def simplify():
  if request.method == "POST":
    exp = request.form
    return str(sympy.S(exp).atoms(sympy.Symbol)), 200
  else:
    return "fuck you", 400

app.debug = True

if __name__ == '__main__':
    app.run(host="127.0.0.1", port=7856)
