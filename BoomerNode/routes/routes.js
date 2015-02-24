var requests = require('requests');
var request = require('request');
var fs = require('fs');



module.exports = function(app) {

	app.get('/', function(req, res) {

		res.end("boomerAPI"); 
	});


	app.post('/login',function(req,res){
		var name = req.body.name;
       	var nickname = req.body.nick;
        var reg_id = req.body.reg_id;
			
		requests.login(name,nickname,reg_id,function (found) {
			console.log(found);
			res.json(found);
		});		
	});

	// app.post('/uploadlocation', function(req,res){
	// 	var lat = req.body.lat;
	// 	var longd = req.body.longd;
	// 	var imageID = req.body.imageID;

	// 	console.log("BODY: ");
	// 	console.log(req.body);
	// 	requests.uploadlocation(lat,longd,imageID,function (found) {
	// 		console.log(found);
	// 		res.json(found);
	// 	});		
	// });
	
	// API to upload boomer (not using requests.js for this )
	app.post('/uploadboomer', function(req, res) {
  		console.log(req.files.image.originalFilename);	
  		console.log(req.files.image.path);

  		var lat = req.body.lat;
		var longd = req.body.longd;

  		var fileName = req.files.image.originalFilename
    	var path = req.files.image.path

    	requests.upload(fileName,path,lat,longd, function(found){
    		console.log(found);
    		res.json(found);
    	});
	});

	// broken
	// app.post('/send',function(req,res){
	// 	var fromu = req.body.from;
	// 	var fromn = req.body.fromn;
 //        var to = req.body.to;
 //        var msg = req.body.msg;

			
	// 	requests.send(from,to,msg,function (found) {
	// 		console.log(found);
	// 		res.json(found);
	// 	});		
	// });

	app.post('/getusers',function(req,res){
		//var mobno = req.body.mobno;

		requests.getusers(function (found) {
			console.log(found);
			res.json(found);
		});		
	});

	app.post('/getfriends',function(req,res){
		//var mobno = req.body.mobno;
		var name = req.body.name;

		requests.getusers(name, function (found) {
			console.log(found);
			res.json(found);
		});		
	});

	app.post('/addfriend',function(req,res){
		//var name = req.body.name;
		var reg_id = req.body.reg_id;
		var friend = req.body.friend;

		requests.addfriend(reg_id,friend,function (found) {
			console.log("Adding friend"+friend)
			console.log(found);
			res.json(found);
		});		
	});

	app.post('/logout',function(req,res){
		var mobno = req.body.mobno;

			
		requests.removeuser(mobno,function (found) {
			console.log(found);
			res.json(found);
		});		
	});

	app.post('/displayAllUsers',function(req,res){
			
		requests.displayAllUsers(function (result) {
			console.log(result);
			res.json(result);
		});		
	});

	
};



