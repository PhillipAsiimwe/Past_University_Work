/*
//Phillip Amanya 
//Jordan King 

*/
var words = [];//keeps track of the word array
var wordwithchords=[];//keeps track of the words and their location in ChordPro format
var Chords=[];//keeps track of the Chords
var Wordlines=[];//Helps maintain the words y location
var Chordlines=[];//Helps maintain the Chords Y location
var tempy=15;//Word lines offset
for (var i=0;i<10;i++){//fills an array of box objects to help align the words
    Wordlines.push({x:0,y:tempy,width:600,height:0});
    tempy+=30;
}
tempy=0;//Chord lines offset
for (var i=0;i<10;i++){//fills an array of box objects to help align the chords
    Chordlines.push({x:0,y:tempy,width:600,height:0});
    tempy+=30;
}

var movingString = {word: "Moving",//Helps with debugging and lets the user know where the files been saved
    x: 100,
    y:100,
    xDirection: 1, //+1 for leftwards, -1 for rightwards
    yDirection: 1, //+1 for downwards, -1 for upwards
    stringWidth: 50, //will be updated when drawn
    stringHeight: 24}; //assumed height based on drawing point size

var timer;
var wordBeingMoved;
var deltaX, deltaY; //location where mouse is pressed
var canvas = document.getElementById('canvas1'); //our drawing canvas
var context = canvas.getContext('2d');

var handlewordsintochords= function () {//function that takes the words and chords and combines them into one array wordwithchords
    words.sort(compare);//sorts the words array
    Chords.sort(compare);//sorts the Chords array
    for (m in Chords){//makes sure that all the chords used variable are set to false
        Chords[m].used=false;
    }
    for (u in words){//Makes sure that all the Words used variables are set to false
        words[u].used=false;
    }
    wordwithchords=[];//Emptys array before its filled.
    var ychordline = 0;//first chord y location
    var ywordline = 15;//first Word y location
    for (var j = 0; j < 10; j++) {//for all the lines
        var lastlastx=0;//var to handle situation when chords is inbetween last word and current word by keeping track of the last word's x
        for (var i = 0; i < words.length; i++) {//Go through all the words in order to see if there is a chord above it.
            var wordobj = words[i];
            var wordlength = context.measureText(wordobj.word).width;
            var firstx = wordobj.x;
            var lastx = firstx + wordlength;//helps determain if a chord is inbetween current word.
            if (wordobj.y == ywordline) {//make sure the words on the current line
                for (k in Chords) {//loop to find the chord above the word
                    var chordObj = Chords[k];
                    if ((chordObj.y>ywordline)||(((chordObj.x > lastx)&&(chordObj.y==ychordline)))){//case to handle if word doesnt have a chord above it
                        wordwithchords.push({word:wordobj.word,x:wordobj.x,y:wordobj.y});
                        wordobj.used=true;
                        break;
                    }else if (chordObj.y == ychordline) {//make sure the chords on the right line
                        if (chordObj.x >= firstx && chordObj.x < lastx) {//check if the chords inbetween the word
                            wordobj.used=true;//boolen to tell know if the words been added
                            var tempword = wordobj.word;
                            var tempchord = chordObj.word;
                            var numbofchar = tempword.length
                            var lengtheachchar = Math.ceil((wordlength / numbofchar));
                            var start = firstx;
                            var last = start + lengtheachchar;
                            var beginword;
                            for (var c = 0; c < numbofchar; c++) {//go through the word and determain exaclty where the chord fits in the word
                                var newchord='';
                                if ((chordObj.x >= start) && (chordObj.x < last)) {// start and last keeps track of the individual char
                                    newchord="["+tempchord+"]";
                                    beginword = '';
                                    if (c==0){
                                        beginword+=newchord;
                                        beginword+=tempword

                                    }else{//if not at the beginning of the word then slice and add
                                        beginword = tempword.substr(0, c);
                                        beginword+=newchord;
                                        beginword += tempword.substr(c);

                                    }
                                    wordwithchords.push({word:beginword,x:wordobj.x,y:wordobj.y});//add the word into the array
                                    chordObj.used=true;
                                }
                                start = last;
                                last += lengtheachchar;
                                //if not found increment next index.
                            }
                            break;
                        }else if(chordObj.x>lastlastx) {//handles case when words inbetween last word and current words
                            var chordword="["+chordObj.word+"]";
                            wordwithchords.push({word:chordword,x:chordObj.x,y:ywordline});
                            chordObj.used=true;

                        }

                    }


                }
            }
        lastlastx=lastx;//keeps track of the last words end
        }
        ywordline+=30;//moves to the next lines once done.
        ychordline+=30;

    }//founds the words that were nor accounted for
    for (d in Chords){
        if(!Chords[d].used){
            var chordObj = Chords[d];
            var chordword="["+chordObj.word+"]";
            var ywordline;
            for (x in Wordlines){
                var wordline=Wordlines[x];
                if((chordObj.y>=wordline.y-17)&&(chordObj.y<wordline.y+13)){
                    ywordline=wordline.y;
                }
            }
            wordwithchords.push({word:chordword,x:chordObj.x,y:ywordline});
            chordObj.used=true;
        }
    }

    for (v in words){
        var wordobj = words[v];
        if (!words[v].used){
            wordwithchords.push({word:wordobj.word,x:wordobj.x,y:wordobj.y});
        }
    }
    wordwithchords.sort(compare);// sorts the array
}


var handleTextArea = function () {//Handles adding into the Text area
    if (words.length != 0 && Chords.length != 0) {
        var lastlastx = 0;
        let textDiv = document.getElementById("text-area");
        textDiv.innerHTML = "";
        textDiv.innerHTML += `<p>`;//opens the paragraph
        var tempy = 0;
        var lastx = 0;
        var spacewidth = context.measureText(" ").width;
        handlewordsintochords();
        for(index in wordwithchords){//goes through array with words in Chord pro form
            var temp_word=wordwithchords[index];
            var wordWidth= context.measureText(temp_word.word).width;
            if(temp_word.y!=tempy) {//means were on a word on a new line
                textDiv.innerHTML += `</p>`;
                textDiv.innerHTML += `<p>`;
                if (temp_word.x>3){//if the first words not at the edge calculates amount of spaces needed
                    var spaces = Math.floor((temp_word.x+3)/spacewidth);
                    for(var i=0;i<spaces;i++){
                        textDiv.innerHTML +=`&nbsp`;
                    }
                }
                textDiv.innerHTML += `${temp_word.word}`;
                tempy = temp_word.y;
                lastx = temp_word.x + wordWidth;

            }else{//the words are on the same lines
                var difference = temp_word.x-lastx;
                if (difference < 0){
                    textDiv.innerHTML += `&nbsp`;
                    textDiv.innerHTML += `${temp_word.word}`;
                    lastx = lastx + spacewidth + wordWidth;
                }else{
                    var spaces = Math.floor(difference/spacewidth)
                    for(var i=0;i<spaces;i++){
                        textDiv.innerHTML +=`&nbsp`;
                    }
                    textDiv.innerHTML+=`${temp_word.word}`;
                    lastx = temp_word.x + wordWidth;
                }
            }
        }
        textDiv.innerHTML+=`</p>`;//closes the paragraph
    }
}





function getWordAtLocation(aCanvasX, aCanvasY){

	  //locate the word near aCanvasX,aCanvasY
	  //Just use crude region for now.
	  //should be improved to using lenght of word etc.
	  
	  //note you will have to click near the start of the word
	  //as it is implemented not
		  for(var i=0; i<words.length; i++){

          if(aCanvasX > words[i].x && aCanvasX < (words[i].x + context.measureText(words[i].word).width)
              && Math.abs(words[i].y - aCanvasY) < 8) return words[i];
	  }
    for(var i=0; i<Chords.length; i++){

        if(aCanvasX > Chords[i].x && aCanvasX < (Chords[i].x + context.measureText(Chords[i].word).width)
            && Math.abs(Chords[i].y - aCanvasY) < 8) return Chords[i];
    }
	  return null;
    }

var drawCanvas = function( value ){
    context.fillStyle = 'white';
    context.fillRect(0,0,canvas.width,canvas.height);//erase canvas
    //hiding drawing lines
    // for (index in Wordlines) {// Prints Words lines
    //     var wordlinestest = Wordlines[index];
    //     context.strokeStyle = 'black';
    //     context.beginPath();
    //     context.fillRect(wordlinestest.x, wordlinestest.y, wordlinestest.width, wordlinestest.height);
    //     context.strokeRect(wordlinestest.x, wordlinestest.y, wordlinestest.width, wordlinestest.height);
    //     context.closePath();
    // }
    // for (index in Chordlines) {//Prints Chordlines
    //     var wordlinestest = Chordlines[index];
    //     context.strokeStyle = 'green';
    //     context.beginPath();
    //     context.fillRect(wordlinestest.x, wordlinestest.y, wordlinestest.width, wordlinestest.height);
    //     context.strokeRect(wordlinestest.x, wordlinestest.y, wordlinestest.width, wordlinestest.height);
    //     context.closePath();
    // }//


    context.font = '12pt Arial';


    if (value){//boolean to know if to place words on lines to help speed
        placeWordsOnLines();//only called when needed.
    }else{
        for(var i=0; i<words.length; i++){  //note i declared as var
            var data = words[i];
            context.fillStyle = 'black';
            context.strokeStyle = 'black ';
            context.beginPath();
            context.fillText(data.word, data.x, data.y);
            context.strokeText(data.word, data.x, data.y);
            context.closePath();
        }
        for(var i=0; i<Chords.length; i++){  //note i declared as var
            var data = Chords[i];
            context.fillStyle = 'cornflowerblue';
            context.strokeStyle = 'blue ';
            context.beginPath();
            context.fillText(data.word, data.x, data.y);
            context.strokeText(data.word, data.x, data.y);
            context.closePath();
        }
    }
    movingString.stringWidth = context.measureText(	movingString.word).width;
    context.fillText(movingString.word, movingString.x, movingString.y);
}

function placeWordsOnLines(){//Matches the words and chords on lines to help process words, allowing chords to always be above words

    for(index in words){
        var tempWord=words[index];
        for (index in Wordlines){
            var wordline=Wordlines[index];
            if((tempWord.y>=wordline.y-17)&&(tempWord.y<wordline.y+13)){
                tempWord.y=wordline.y;
            }
        }
    }
    for (index in Chords){
        var tempChord=Chords[index];
        for (index in Chordlines){
            var chordline=Chordlines[index];
            if((tempChord.y>=chordline.y-17)&&(tempChord.y<chordline.y+13)){//offset to allow the chord to be pulled to the right line
                tempChord.y=chordline.y;
            }
        }

    }
    words.sort(compare);
    Chords.sort(compare);

}
function handleTimer(){
    movingString.x = (movingString.x + 5*movingString.xDirection);
    movingString.y = (movingString.y + 5*movingString.yDirection);

    //keep moving string within canvas bounds
    if(movingString.x + movingString.stringWidth > canvas.width) movingString.xDirection = -1;
    if(movingString.x < 0) movingString.xDirection = 1;
    if(movingString.y > canvas.height) movingString.yDirection = -1;
    if(movingString.y - movingString.stringHeight < 0) movingString.yDirection = 1;

    drawCanvas(false);
}

function handleMouseDown(e){
	
	//get mouse location relative to canvas top left
	var rect = canvas.getBoundingClientRect();
    //var canvasX = e.clientX - rect.left;
    //var canvasY = e.clientY - rect.top;
    var canvasX = e.pageX - rect.left; //use jQuery event object pageX and pageY
    var canvasY = e.pageY - rect.top;

	wordBeingMoved = getWordAtLocation(canvasX, canvasY);
	//console.log(wordBeingMoved.word);
	if(wordBeingMoved != null ){
	   deltaX = wordBeingMoved.x - canvasX; 
	   deltaY = wordBeingMoved.y - canvasY;
	   //document.addEventListener("mousemove", handleMouseMove, true);
       //document.addEventListener("mouseup", handleMouseUp, true);
	$("#canvas1").mousemove(handleMouseMove);
	$("#canvas1").mouseup(handleMouseUp);
	   
	}

    // Stop propagation of the event and stop any default 
    //  browser action

    e.stopPropagation();
    e.preventDefault();
	
	drawCanvas(false);
	}
	
function handleMouseMove(e){

	
	//get mouse location relative to canvas top left
	var rect = canvas.getBoundingClientRect();
    var canvasX = e.pageX - rect.left;
    var canvasY = e.pageY - rect.top;
	
	wordBeingMoved.x = canvasX + deltaX;
	wordBeingMoved.y = canvasY + deltaY;
	
	e.stopPropagation();
	
	drawCanvas(false);
	}
	
function handleMouseUp(e){

	e.stopPropagation();
	
    //$("#canvas1").off(); //remove all event handlers from canvas
    //$("#canvas1").mousedown(handleMouseDown); //add mouse down handler

	//remove mouse move and mouse up handlers but leave mouse down handler
    $("#canvas1").off("mousemove", handleMouseMove); //remove mouse move handler
    $("#canvas1").off("mouseup", handleMouseUp); //remove mouse up handler

	drawCanvas(true); //redraw the canvas
	}
	
//JQuery Ready function -called when HTML has been parsed and DOM
//created
//can also be just $(function(){...});
//much JQuery code will go in here because the DOM will have been loaded by the time
//this runs

    //KEY CODES
	//should clean up these hard coded key codes
	var ENTER = 13;
	var RIGHT_ARROW = 39;
	var LEFT_ARROW = 37;
	var UP_ARROW = 38;
	var DOWN_ARROW = 40;


function handleKeyDown(e){
	

	var dXY = 5; //amount to move in both X and Y direction
	if(e.which == UP_ARROW && movingBox.y >= dXY) 
	   movingBox.y -= dXY;  //up arrow
	if(e.which == RIGHT_ARROW && movingBox.x + movingBox.width + dXY <= canvas.width) 
	   movingBox.x += dXY;  //right arrow
	if(e.which == LEFT_ARROW && movingBox.x >= dXY) 
	   movingBox.x -= dXY;  //left arrow
	if(e.which == DOWN_ARROW && movingBox.y + movingBox.height + dXY <= canvas.height) 
	   movingBox.y += dXY;  //down arrow
	
    var keyCode = e.which;
    if(keyCode == UP_ARROW | keyCode == DOWN_ARROW){
       //prevent browser from using these with text input drop downs	
       e.stopPropagation();
       e.preventDefault();
	}
	if(e.which==13){
        handleSubmitButton();

    }

}




function handleSubmitButton () {
    let textDiv = document.getElementById("text-area");
   
    var userText = $('#userTextField').val(); //get text from user text input field
	if(userText && userText != ''){


	   //user text was not empty
	   var userRequestObj = {text: userText}; //make object to send to server
       var userRequestJSON = JSON.stringify(userRequestObj); //make json string
	   //Prepare a POST message for the server and a call back function
	   $.post("fetchmysong", userRequestJSON, function(data, status){
			console.log(" My data: " + data);
			console.log("typeof: " + typeof data);
			var responseObj = data;
			movingString.word = responseObj.text;
			words=[];
			//handles words and puts them on the canvas and puts the words into the innerHTML.
			if(responseObj.songLines) {
                words = [];
                Chords=[];
                textDiv.innerHTML = "";
                var songlines = responseObj.songLines;
                handlewrds(songlines);//handles the words passed in helps place them on the canvas
                placeWordsOnLines();//places the words passed on the lines
                handleRefreshButton();//displays the words in chord pro

            }
			});
	}

    drawCanvas(false);//redraw the canvas



}

function handlewrds(wordarray) {//Handles the words and their location passed by line

    var chord;
	var yalignment=40;
    var wordheightWITHspacing=30;
	for (var i=0; i<wordarray.length;i++){
        var xalignment=3;
        var xchordalign=3;
		var eachLineWthWrdsSplit= wordarray[i].split(" ");//splits the words by space.
        for(index in eachLineWthWrdsSplit){
            eachLineWthWrdsSplit[index] = eachLineWthWrdsSplit[index].replace(/(\r\n|\n|\r)/gm, "");
            if (eachLineWthWrdsSplit[index]==" "||eachLineWthWrdsSplit[index]=="  "){//removes any random spaces
                continue;
            }
            if (xchordalign>xalignment){
                xalignment=xchordalign;
            }else{
                xchordalign=xalignment;
            }
            var wholeWords=eachLineWthWrdsSplit[index];
            while(wholeWords.indexOf('[')>=0){//finds the Chors in the word
                var leftindex=wholeWords.indexOf('[');
                var rightindex=wholeWords.indexOf(']');
                chord=wholeWords.substring(leftindex,rightindex+1);
                wholeWords=wholeWords.replace(chord,"");
                chord=chord.substr(1).slice(0,-1);
                var chordWidth=((context.measureText(chord).width));
                Chords.push({word:chord,x:xchordalign,y:(yalignment-15)});//places the chord into an arrya with the the y value adjusted above the words
                xchordalign+=chordWidth;
            }
            var wordWidth=((context.measureText(wholeWords).width) + (context.measureText("  ").width));
            words.push({word:wholeWords,x:xalignment,y:yalignment});
            xalignment+=wordWidth;
        }

		yalignment+=wordheightWITHspacing;
	}
}

var handleString = function () {//function to handle strings, returns the chords in chord pro form
    let text='';
    if (wordwithchords.length!=0) {
        var lastlastx=0;
        var tempy=0;
        var lastx=0;
        var spacewidth = 7 ;
        for(index in wordwithchords){
            var temp_word=wordwithchords[index];
            var wordWidth= context.measureText(temp_word.word).width;
            if(temp_word.y!=tempy) {
                text += `\r\n`;
                if (temp_word.x>3){
                    var spaces = Math.floor((temp_word.x+3)/spacewidth);
                    for(var i=0;i<spaces;i++){
                        text +=` `;
                    }
                }
                text += temp_word.word;
                tempy = temp_word.y;
                lastx = temp_word.x + wordWidth;

            }else{
                var difference = temp_word.x-lastx;
                if (difference < 0){
                    text += ` `;
                    text += temp_word.word;
                    lastx = lastx + spacewidth + wordWidth;
                }else{
                    var spaces = Math.floor(difference/spacewidth)
                    for(var i=0;i<spaces;i++){
                        text +=` `;
                    }
                    text+=temp_word.word;
                    lastx = temp_word.x + wordWidth;

                }

            }


        }
        text+=`\r\n`;

    }
    return text;
}

function compare(a,b) {//compare fucntion to determain the sorted array in terms of x and y
    if (a.y-b.y==0){
        return(a.x-b.x);
    }else if(a.y>b.y){
        return  1;
    }else {
        return -1;
    }
}


function handleRefreshButton() {//reacts when refresh button is pushed
    handleTextArea();
    drawCanvas(false);
}




function handleSaveasButton () {//reacts when saveAs Button is pushed.
    var userRequestObj={};
    var song = handleString();
    var userText = $('#userTextField').val();
    if(userText && userText != '') {
        userRequestObj.location= userText
    }else{
        userRequestObj.location="UnKnown Song";
    }
    userRequestObj.song = song;
    movingString.word="./"+userRequestObj.location+".TXT";
    var userRequestJSON = JSON.stringify(userRequestObj);
    $.post("saveSong",userRequestJSON);

}




$(document).ready(function(){
	//This is called after the broswer has loaded the web page
	
	//add mouse down listener to our canvas object
	$("#canvas1").mousedown(handleMouseDown);
	
	//add key handler for the document as a whole, not separate elements.	
	$(document).keydown(handleKeyDown);
	//$(document).keyup(handleKeyUp);
		
	timer = setInterval(handleTimer, 100); 
    //timer.clearInterval(); //to stop
	
	drawCanvas(false);
});

