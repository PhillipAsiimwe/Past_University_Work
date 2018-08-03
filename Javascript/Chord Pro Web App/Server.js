//Phillip Amanya 
//Jordan King

var songFiles={//song location array
    "Peaceful Easy Feeling": "songs/Peaceful Easy Feeling.txt",
    "Sister Golden Hair": "songs/Sister Golden Hair.txt",
    "Brown Eyed Girl": "songs/Brown Eyed Girl.txt"
};

//Server Code
var fs = require("fs");//imported to read and write to files
var url = require("url");//imported to parse url strings
var http = require("http")//imported for HTTP
var ROOT_DIR = "html";// Directory to serve static files from
var MIME_TYPES = {
    css: "text/css",
    gif: "image/gif",
    htm: "text/html",
    html: "text/html",
    ico: "image/x-icon",
    jpeg: "image/jpeg",
    jpg: "image/jpeg",
    js: "text/javascript", //should really be application/javascript
    json: "application/json",
    png: "image/png",
    txt: "text/plain"
};

var get_mime = function(filename) {
    var ext, type;
    for (ext in MIME_TYPES) {
        type = MIME_TYPES[ext];
        if (filename.indexOf(ext, filename.length - ext.length) !== -1) {
            return type;
        }
    }
    return MIME_TYPES["txt"];
};
//Create the server
http.createServer(function(request, response) {
    var urlObj = url.parse(request.url, true, false);
    console.log("\n============================");
    console.log("PATHNAME: " + urlObj.pathname);
    console.log("REQUEST: " + ROOT_DIR + urlObj.pathname);
    console.log("METHOD: " + request.method);

    var receivedData = "";

    //attached event handlers to collect the message data
    request.on("data", function(chunk) {
        receivedData += chunk;
    });

    //event handler for the end of the message
    request.on("end", function() {
        //Handle the client POST requests
        //console.log('received data: ', receivedData);

        //If it is a POST request then we will check the data.
        if (request.method=="POST" && urlObj.pathname == "/saveSong"){
            var dataObj = JSON.parse(receivedData);
           // var songlines = dataObj.song;
            var songPAth="songs/hahah.txt";
            if(dataObj.location){
                songPAth = "songs/"+dataObj.location+".txt"
            }
            var song=dataObj.song;
            // for (i in songlines){
            //     if (songlines[i]!=""){
            //         song+=songlines[i]+'\r\n';
            //
            //     }
            // }
            console.log("Song to save:");
            console.log(song);
            fs.writeFile(songPAth,song,function (error){
                if (error){
                    console.log("ERROR");
                }else{
                    console.log("Success");
                }

            });

        }else if (request.method == "POST") {
            var dataObj = JSON.parse(receivedData);
            console.log("received data object: ", dataObj);
            console.log("type: ", typeof dataObj);
            console.log("USER REQUEST: " + dataObj.text);
            var songPath = ""; //path to requested song file
            for (title in songFiles) {
                if (title === dataObj.text) {
                    songPath = songFiles[title];//songPath will have the location of the song file.

                }
            }

            //look for the song file in the /songs directory
            var returnObj = {};
            if (songPath === "") {//if song path not found.
                returnObj.text=dataObj.text + " NOT FOUND";
                response.writeHead(200, { "Content-Type": MIME_TYPES["json"] });
                response.end(JSON.stringify(returnObj)); //send just the JSON object
            } else {
                //Found the song file
                fs.readFile(songPath, function(err, data) {
                    //Read song data file and send lines and chords to client
                    if (err) {//handle the file error incase the file was found but cant be read
                        returnObj.text = "FILE READ ERROR";
                        response.writeHead(200, { "Content-Type": MIME_TYPES["json"] });
                        response.end(JSON.stringify(returnObj));
                    } else {
                        var fileLines = data.toString().split("\n");//split the data into lines.
                        for (i in fileLines) {
                            fileLines[i] = fileLines[i].replace(/(\r\n|\n|\r)/gm, "");
                        }
                        returnObj.text = songPath;
                        returnObj.songLines = fileLines;
                        response.writeHead(200, { "Content-Type": MIME_TYPES["json"] });
                        response.end(JSON.stringify(returnObj));
                    }
                });
            }
        }

        else if (request.method == "GET") {
            //handle GET requests as static file requests
            var filePath = ROOT_DIR + urlObj.pathname;
            if (urlObj.pathname === "/") filePath = ROOT_DIR + "/index.html";

            fs.readFile(filePath, function(err, data) {
                if (err) {
                    //report error to console
                    console.log("ERROR: " + JSON.stringify(err));
                    //respond with not found 404 to client
                    response.writeHead(404);
                    response.end(JSON.stringify(err));
                    return;
                }
                response.writeHead(200, { "Content-Type": get_mime(filePath) });
                response.end(data);
            });
        }
    });
}).listen(3000);
console.log("Server Running at http://127.0.0.1:3000  CNTL-C to quit");


