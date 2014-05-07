var express = require('express');
var http = require('http');
var path = require('path');
var Busboy = require('busboy');
var fs = require('fs')
var app = express();
var upload_app = express();
var net = require("net");

app.use(express.bodyParser());

var hld = {};
var id = "yyyppp";
hld[id] = 'cos * sin';

net.createServer(function (socket){

socket.on("data", function(data){
var json_msg = JSON.parse(String(data));
hld[json_msg["id"]] = json_msg["results"];
});


}).listen(8100);

app.configure(function(){app.set('port', 8000)});

app.get('/', function(req, res){
   var id = req.param('id');
   console.log ("received get request " + id);

   res.contentType('application/json');

   var json_res = JSON.stringify (hld[id]);
   console.log(json_res); 
   res.send(json_res);
   res.end();
});


app.post('/', function(req, res){
   var id = req.param('id');
   var sin = req.param('sin_checkbox');
   var cos = req.param('cos_checkbox');
   console.log('id ' + id);
   console.log('sin ' + sin);
   console.log('cos ' + cos);
   var s = net.Socket();
   s.connect(9000);
   s.write (id + ';' + sin + ';' + cos);
   s.end();
});



http.createServer(app).listen(app.get('port'), function(){
  console.log("Express server listening on port " + app.get('port'));
});



upload_app.configure(function(){upload_app.set('port', 8200)});

upload_app.post('/', function(req, res){

   console.log("got file");
   var id = req.param('id');
   console.log(id);
   var busboy = new Busboy({ headers: req.headers });
    busboy.on('file', function(fieldname, file, filename, encoding, mimetype) {
      var saveTo = '/home/ubuntu/content_farm/igniipotent/angular-regress/app/java/' + id + '.csv';
      file.pipe(fs.createWriteStream(saveTo));
    });
    busboy.on('finish', function() {
      var redirect_url = 'http://www.igniipotent.com/angular-regress/app/index.html#/process/' + id;
      res.redirect(redirect_url);
    });
    return req.pipe(busboy);
});



http.createServer(upload_app).listen(upload_app.get('port'), function(){
  console.log("Upload server listening on port " + upload_app.get('port'));
});


