var app = require('http').createServer(handler)
var io = require('socket.io')(app);
var fs = require('fs');

app.listen(80,'192.168.2.8'); //para utilizar una ip
//app.listen(80); //para ser usado en local
function handler (req, res) {
//  fs.readFile(__dirname + '/socket.html',
//  function (err, data) {
//    if (err) {
//      res.writeHead(500);
//      return res.end('Error loading index.html');
//    }
//
//    res.writeHead(200);
//    res.end(data);
//  });
str =req.rawHeaders[11];
str1 = str.replace('http://localhost',str);
//console.log(req);
console.log(req.url);
//if (str1==='/TouchControl.html'){
  fs.readFile(__dirname + req.url,
  function (err, data) {
    if (err) {
      res.writeHead(500);
      return res.end('Error loading index.html');
    }

    res.writeHead(200);
    res.end(data);
  });
//}
}

io.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  console.log(socket.id);
  socket.on('my other event', function (data) {
    console.log(socket.id);
    console.log(data);
    
       socket.broadcast.emit('item', { x: data.x,y:data.y }); 
    
     //
  });
  
});