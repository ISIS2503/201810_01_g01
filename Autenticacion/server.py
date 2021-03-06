import base64
import hashlib
import redis
from flask import Flask, Response, request
 
app = Flask(__name__)
r = redis.Redis()
m = hashlib.md5()
 
@app.route('/auth', methods=['POST'])
def auth():
    response = Response(content_type='text/plain', status=403)
 
    try:
      auth = request.headers.get('Authorization')
      token = auth.split(' ')[1]
      data = base64.b64decode(token).decode("utf-8").split(':')
      username = data[0]
      password = bytes(data[1], 'utf-8')
      m.update(password)
      if r.get(username).decode("utf-8") == m.hexdigest():
          response.status_code = 200
    except:
      pass
    return response
 
@app.route('/superuser', methods=['POST'])
def superuser():
    response =  Response(content_type='text/plain', status=403)
    try:
      auth = request.headers.get('Authorization')
      token = auth.split(' ')[1]
      print(token)
      data = base64.b64decode(token).decode("utf-8").split(':')
      username = data[0]
      # password = data[1]
      if username == 'microcontrolador': #  and password == '13579':
          response.status_code = 200
    except:
      pass
    return response
 
@app.route('/acl', methods=['POST'])
def acl():
    response =  Response(content_type='text/plain', status=200)
    return response
 
if __name__ == '__main__':
    app.run()
    response =  Response(content_type='text/plain', status=200)
    return response
 
if __name__ == '__main__':
    app.run()