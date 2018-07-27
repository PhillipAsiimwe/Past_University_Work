/*
phillip Amanya 
Jordan King Before you run this app first execute
>npm install
to install npm modules dependencies listed in package.json file
Then launch this server:
>node chatServer.js

To test open several browsers to: http://localhost:3000/chatClient.html

*/
const app = require('http').createServer(handler)
const io = require('socket.io')(app) //wrap server app in socket io capability
const fs = require('fs') //file system to server static files
const url = require('url');  //to parse url strings
const PORT = process.env.PORT || 3000
const Matter= require('matter-js');//Mtter.js request
var colour={red:false,blue:false};//keeps track of the colour state of the paddles, true shows a darker, working paddle
var control=[];//keeps track of which Socket has control of the paddle, first position means control, red paddle
var Engine,World,Bodies,Body,Event//initialze the matter.js properties
var Puck,Paddle1,Paddle2,Ground,Top,LeftWall,RightWall,Leftgoal,Rightgoal//Different objects in my world.
var puckradius=30;
var paddleradius=50;
var engine,world;
var useres={};//keeps track of the users and their nickname
var score={left:0,right:0};//keeps track of the score 
var  started= false;
var tosend={};//object that well constantly be sending data

const ROOT_DIR = 'www' //dir to serve static files from

const MIME_TYPES = {
    'css': 'text/css',
    'gif': 'image/gif',
    'htm': 'text/html',
    'html': 'text/html',
    'ico': 'image/x-icon',
    'jpeg': 'image/jpeg',
    'jpg': 'image/jpeg',
    'js': 'application/javascript',
    'json': 'application/json',
    'png': 'image/png',
    'txt': 'text/plain'
}

function get_mime(filename) {
    let ext, type;
    for (let ext in MIME_TYPES) {
        type = MIME_TYPES[ext]
        if (filename.indexOf(ext, filename.length - ext.length) !== -1) {
            return type
        }
    }
    return MIME_TYPES['txt']
}

app.listen(PORT) //start server listening on PORT


function handler(request,response){
     let urlObj = url.parse(request.url, true, false)
     console.log('\n============================')
	   console.log("PATHNAME: " + urlObj.pathname)
     console.log("REQUEST: " + ROOT_DIR + urlObj.pathname)
     console.log("METHOD: " + request.method)

	 let filePath = ROOT_DIR + urlObj.pathname
	 if(urlObj.pathname === '/') filePath = ROOT_DIR + '/index.html'

     fs.readFile(filePath, function(err,data){
       if(err){
		  //report error to console
          console.log('ERROR: ' + JSON.stringify(err))
		  //respond with not found 404 to client
          response.writeHead(404);
          response.end(JSON.stringify(err))
          return
         }
         response.writeHead(200, {'Content-Type': get_mime(filePath)})
         response.end(data)
       });

 }
//Handle all the socket inputs from the client
io.on('connection', function(socket){

    init(!started);
    started= true;
    setInterval(function() {
        TO_send();//function defined below to package all the variables currently in the world
        var send =JSON.stringify(tosend);
        socket.emit("sending",send);
    }, 1000 / 60);
    socket.on("direction",function (direction) {//handles direction inputs from client
        var hascnt=false;
        for(var i=0;i<2;i++){
            if(control[i]==socket.id){
                hascnt=true
            }
        }
        if (hascnt)handledirection(direction)
    })
    socket.on("message",function (data) {//gets chat server request 
        io.local.emit("updateMessage",data);//semds that to all the clients 

    })
    socket.on("UserName",function (user) {//handles username push from client
        var tempuser=JSON.parse(user);
        useres[tempuser.socket]=tempuser.text;
    })
    socket.on("control",function (cntr) {//decides how and who gets control at all times 
        var givecontrol=JSON.parse(cntr)
        if (givecontrol==2){
            for(var i=0;i<2;i++){
                if(control[i]==socket.id){
                    delete control[i];
                }
            }
        }else {
            for(var i=0;i<2;i++) {
                if (control[givecontrol]==undefined){
                    control[givecontrol]=socket.id;
                }
            }
        }
        paddlecolour();
    })

    socket.on("disconnect",function (data) {// handles disconnection
        delete useres[socket.id];
        for(var i=0;i<2;i++){
            if(control[i]==socket.id){
                delete control[i];
            }
        }
        paddlecolour();

    })

})
function paddlecolour() {// sets colour variable
    if(control[0]==undefined){
        colour.blue=false;
    }else colour.blue=true;
    if (control[1]==undefined){
        colour.red=false;
    }else {
        colour.red=true;
    }
    if(control[0]==undefined&&control[1]==undefined){
        score={left:0,right:0};

    }
    var sendcolour=JSON.stringify(colour)
    io.local.emit("colour",sendcolour);
}

//function to start the engine
function init( data ) {
    if(data) {
        Engine = Matter.Engine,
            Body = Matter.Body,
            // Render = Matter.Render,
            World = Matter.World,
            Event = Matter.Events,
            Bodies = Matter.Bodies;

// create an engine
        engine = Engine.create();
        world = engine.world;
        world.gravity.y = 0;
//create elements
        Paddle1 = Bodies.circle(220, 300, paddleradius, {density: 2, frictionAir: 0, frictionStatic: 0.3});
        Puck = Bodies.circle(620, 300, puckradius, {
            restitution: 1,
            friction: 0,
            frictionStatic: 0,
            density: 1,
            frictionAir: 0
        });
        Puck.label = "puck"
        Paddle2 = Bodies.circle(1000, 300, paddleradius, {density: 2, frictionAir: 0, frictionStatic: 0.3})
        Ground = Bodies.rectangle(600, 700, 1400, 220, {isStatic: true});
        Top = Bodies.rectangle(600, -100, 1400, 220, {isStatic: true});
        LeftWall = Bodies.rectangle(-140, 300, 300, 640, {isStatic: true});
        Leftgoal = Bodies.rectangle(-100, 300, 238, 100, {isSensor: true});
        Leftgoal.label = "leftgoal"
        RightWall = Bodies.rectangle(1398, 300, 420, 640, {isStatic: true});
        Rightgoal = Bodies.rectangle(1300, 300, 242, 100, {isSensor: true});
        Rightgoal.label = "rightgoal"


        World.add(world, [Paddle1, Puck, Paddle2, Ground, Top, RightWall, LeftWall, Leftgoal, Rightgoal]);

        // add all of the bodies to the world
        //World.add(world, [Paddle1,Paddle2,Puck,top,ground,left,right]);
        // tell the engine to run
        setInterval(function () {
            Engine.update(engine, 1000 / 60);
        }, 1000 / 60);
        Event.on(engine, "collisionStart", function (data) {
            var obj = data.pairs[0];
            if ((obj.bodyA.label == "leftgoal" || obj.bodyA.label == "puck") && (obj.bodyB.label == "leftgoal" || obj.bodyB.label == "puck")) {
                score.right++;
                Puck.isStatic = true;
                goal("right");
                init(true);
            } else if ((obj.bodyA.label == "rightgoal" || obj.bodyA.label == "puck") && (obj.bodyB.label == "rightgoal" || obj.bodyB.label == "puck")) {
                score.left++;
                Puck.isStatic = true;
                goal("left")
                init(true);
            }


        })
    }

}
function goal(data) {// handles what to do when a goal is scored
    var goal={who:"",points:JSON.stringify(score)}
    if(data=="right"){
       goal.who=useres[control[1]];
    }else if(data=="left"){
        goal.who=useres[control[0]];
    }
    var JSongoal=JSON.stringify(goal)
    io.local.emit("goal",JSongoal);

}
function  TO_send() {
    var puckpos=JSON.stringify(Puck.position);
    var puckrad=JSON.stringify(Puck.circleRadius);
    var puck={pos:puckpos,rad:puckrad};
    var paddle1pos=JSON.stringify(Paddle1.position);
    var paddle2pos=JSON.stringify(Paddle2.position);
    var paddle1rad=JSON.stringify(Paddle1.circleRadius);
    var paddle2rad=JSON.stringify(Paddle2.circleRadius);
    var paddle1={pos:paddle1pos,rad:paddle1rad};
    var paddle2={pos:paddle2pos,rad:paddle2rad};
    tosend.puck=JSON.stringify(puck);
    tosend.paddle1=JSON.stringify(paddle1);
    tosend.paddle2=JSON.stringify(paddle2);

}
function handledirection(direction) {
    var dxy=30;
    if(direction=="up") Body.applyForce(Paddle1,Paddle1.position,{x:0,y:-dxy});
    if(direction=="right")Body.applyForce(Paddle1,Paddle1.position,{x:dxy,y:0});
    if(direction=="left")Body.applyForce(Paddle1,Paddle1.position,{x:-dxy,y:0});
    if(direction=="down")Body.applyForce(Paddle1,Paddle1.position,{x:0,y:dxy});
    if(direction=="lup") Body.applyForce(Paddle2,Paddle2.position,{x:0,y:-dxy});
    if(direction=="lright")Body.applyForce(Paddle2,Paddle2.position,{x:dxy,y:0});
    if(direction=="lleft")Body.applyForce(Paddle2,Paddle2.position,{x:-dxy,y:0});
    if(direction=="ldown")Body.applyForce(Paddle2,Paddle2.position,{x:0,y:dxy});

}


console.log(`Server Running at port ${PORT}  CNTL-C to quit`);
console.log(`To Test: open several browsers to: http://localhost:3000/chatClient.html`)
