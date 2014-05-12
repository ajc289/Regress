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
   var square = req.param('square_checkbox');
   var square_root = req.param('square_root_checkbox');
   var sin = req.param('sin_checkbox');
   var cos = req.param('cos_checkbox');
   var constant_lower_bound = req.param('constant_lower_bound');
   var constant_upper_bound = req.param('constant_upper_bound');
   var s = net.Socket();

   s.connect(9000);
   s.write (id + ';' + square + ';' + square_root + ';' + sin + ';' + cos + ';' + constant_lower_bound + ';' + constant_upper_bound);
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
   var saveTo = '/home/ubuntu/content_farm/igniipotent/angular-regress/app/java/' + id + '.csv';
   var stream = fs.createWriteStream(saveTo);
   var data_size = 0;
   var busboy = new Busboy({ headers: req.headers, limits : {fileSize: 1024 * 1024 * 2}});
    busboy.on('file', function(fieldname, file, filename, encoding, mimetype) {
      file.on('data',function(data){stream.write(data);data_size += data.length;});
    });
    busboy.on('finish', function() {
      var redirect_url = '';
      if (data_size >= 1024 * 1024 * 2)
      {
         redirect_url = 'http://www.igniipotent.com/angular-regress/app/index.html#/filepick_size_error/';
      }
      else
      {
         redirect_url = 'http://www.igniipotent.com/angular-regress/app/index.html#/process/' + id;
      }
      res.redirect(redirect_url);
      stream.end();
    });
    return req.pipe(busboy);
});


http.createServer(upload_app).listen(upload_app.get('port'), function(){
  console.log("Upload server listening on port " + upload_app.get('port'));
});


