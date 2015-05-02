var app = require('http').createServer(handler)
var io = require('socket.io')(app);
var fs = require('fs');
var desk = "";
app.listen(80,'192.168.1.14'); //para utilizar una ip
var osc = require('node-osc');

var client = new osc.Client('192.168.1.10', 3333);

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
  //io.sockets.connected[socket.id].emit("message", "prueba");
  
  socket.on('my other event', function (data) {
    //console.log(socket.id);
    console.log(data);
    
    if (data.desk==1){
    desk=socket.id;
    }
    if(desk!==""){
    io.sockets.connected[desk].emit("message", {id:socket.id, x: data.x,y:data.y ,b:data.b});
    var msg =  new osc.Message('/address');
    msg.append(socket.id);

    if(data.x===undefined){
     data.x=0;   
    }
    msg.append(data.x);
    if(data.y===undefined){
     data.y=0;   
    }
    msg.append(""+data.y);
    if(data.b===undefined){
     data.b=0;   
    }
    msg.append(""+data.b);
 
// client.send(msg)
    client.send( msg);
      }
         // socket.broadcast.emit('item', {id:socket.id, x: data.x,y:data.y }); 
    
     //
  });
  
});