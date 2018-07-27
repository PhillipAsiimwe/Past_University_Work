var lRIGHT_ARROW=108;
var lLEFT_ARROW=106;
var lUP_ARROW=105;
var lDOWN_ARROW=107;
var RIGHT_ARROW = 100;
var LEFT_ARROW = 97;
var UP_ARROW = 119;
var DOWN_ARROW = 115;
var red, blue,username
var priority=0;
var socket = io('http://' + window.document.location.host)
var canvas = document.getElementById('canvas1'); //our drawing canvas
var textDiv= document.getElementById("text-area");
textDiv.innerHTML='Welcome,To start write your name<br> For blue paddel press 1, control with "W,A,S,D",<br>For red ' +
    'paddle press 2, control with "I,J,K,L"<br>Give up control of paddle by pressing 0, Feel free to chat with other connected users<p></p> Enjoy';
var context = canvas.getContext('2d');
var base_image=new Image();
base_image.src='airhockey.png'
var recieved={
    puck:{},
    paddle1:{},
    paddle2:{}
}
socket.on("goal",function (data) {
    var parsedgoal=JSON.parse(data);
    textDiv.innerHTML+=`<p>`;
    textDiv.innerHTML+=`${parsedgoal.who}`+" Just Scored!!!"
    textDiv.innerHTML+=`<br>`;
    var parsedstat=JSON.parse(parsedgoal.points);
    textDiv.innerHTML+=`${parsedstat.left}`+" Blue "+`${parsedstat.right}`+" Red ";
    textDiv.innerHTML+=`</p>`;


})
socket.on("updateMessage",function (data) {
    var message=JSON.parse(data);
    textDiv.innerHTML+=`<p>`;
    textDiv.innerHTML+=`${message.username}`+" : ";
    textDiv.innerHTML+=`<br>`;
    textDiv.innerHTML+=`${message.message}`;
    textDiv.innerHTML+=`</p>`;

})
socket.on("colour",function (data) {
    var tempcolour=JSON.parse(data);
    red=tempcolour.red;
    blue=tempcolour.blue;
    drawCanvas()

})

socket.on("sending",function (data) {
    handleRecievedData(data);
    drawCanvas();

})

function handleRecievedData(data) {
    var tempobj=JSON.parse(data);
    for(var i in tempobj){
        var tempthing=JSON.parse(tempobj[i]);
        var temppos=JSON.parse(tempthing.pos);
        var temprad=JSON.parse(tempthing.rad);
        recieved[i].pos=temppos;
        recieved[i].rad=temprad;
    }

}
var drawCanvas = function(){
    // context.fillStyle = 'white';
    // context.fillRect(0,0,canvas.width,canvas.height); //erase canvas
        context.drawImage(base_image,0,0,1200,600);
        //draw blue paddle
        if(blue){
            context.fillStyle = ' #0000ff';
            context.strokeStyle = ' #0000ff';
        }else {
            context.fillStyle = ' #9999ff';
            context.strokeStyle = ' #9999ff';
        }
        context.beginPath();
        context.arc(recieved.paddle1.pos.x, //x co-ord
            recieved.paddle1.pos.y, //y co-ord
            recieved.paddle1.rad, //radius
            0, //start angle
            2*Math.PI //end angle
        );
        context.fill();

        //draw Red Paddle
        if(red){
            context.fillStyle = '#ff0000';
            context.strokeStyle = '#ff0000';
        }else {
            context.fillStyle = '#ff8080';
            context.strokeStyle = '#ff8080';
        }

        context.beginPath();
        context.arc(recieved.paddle2.pos.x, //x co-ord
            recieved.paddle2.pos.y, //y co-ord
            recieved.paddle2.rad, //radius
            0, //start angle
            2*Math.PI //end angle
        );
        context.fill();

        //draw PUCK
        context.beginPath();
        context.fillStyle = 'black';
        context.strokeStyle = 'black';

        context.arc(recieved.puck.pos.x, //x co-ord
            recieved.puck.pos.y, //y co-ord
            recieved.puck.rad, //radius
            0, //start angle
            2*Math.PI //end angle
        );
        context.fill();

        context.fillStyle = 'Black';
        context.strokeStyle = 'blue';
        context.fillRect(0,590,1400,30);
        context.fillRect(0,0,1400,10);
        context.fillRect(0,0,10,600);
        context.fillRect(1190,0,10,600);
        context.fillStyle="yellow";
        context.fillRect(0,250,11,100);
        context.fillRect(1189,250,11,100);
}

function handleSubmitButton () {

    var userText = document.getElementById('userTextField').value; //get text from user text input field
    if (userText && userText != '') {
        if(username){
            var message=JSON.stringify({message:userText,username:username});
            socket.emit("message",message);

        }else {
            var userRequestObj = {text: userText,socket:socket.id};
            var userRequestJSON = JSON.stringify(userRequestObj);
            document.getElementById('userTextField').value = ''; //clear the user text field
            socket.emit("UserName",userRequestJSON);
            username=userText;

        }


    }
}

function handlekeypress(e) {
    if(e.which==13){
        handleSubmitButton();
        e.stopPropagation();
        e.preventDefault();
    }
    var cntr, push = true;
    if (e.which == 49) {
        priority = 0;
        cntr = JSON.stringify(priority);
    }
    if (e.which == 50) {
        priority = 1
        cntr = JSON.stringify(priority);
    }
    if (e.which == 48) {
        priority = 2
        cntr = JSON.stringify(priority);
    }
    if (cntr != null) {
        socket.emit("control", cntr);
    }
    var direcetion = '';
    if (priority == 0) {
        if (e.which == UP_ARROW) direcetion = "up";
        if (e.which == RIGHT_ARROW) direcetion = "right";
        if (e.which == LEFT_ARROW) direcetion = "left";
        if (e.which == DOWN_ARROW) direcetion = "down";
    } else if (priority == 1) {
        if (e.which == lUP_ARROW) direcetion = "lup";
        if (e.which == lRIGHT_ARROW) direcetion = "lright";
        if (e.which == lLEFT_ARROW) direcetion = "lleft";
        if (e.which == lDOWN_ARROW) direcetion = "ldown";
    } else {
        push = false;
    }
    if (push) {
        socket.emit("direction", direcetion);
    }

}
