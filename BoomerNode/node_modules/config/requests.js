var mongoose = require('mongoose');
var request = require('request');
var fs = require('fs');
var user = require('config/models');
var img = require('config/models/img');
var geo = require('config/models/geo');
mongoose.connect('mongodb://localhost:27017/boomerDB');


exports.login = function(name,nick,reg_id,callback) {

var newuser = new user({ 
	name: name,
	nick: nick, 
	reg_id: reg_id,
  friends: [] });

// check to see if user exists
user.find({nick:nick},function(err,users){

var len = users.length;

if(len == 0){
   // save user in db cause hes the one one w/ that name
 	newuser.save(function (err) {	
	callback({'response':"Sucessfully Registered"});		
});
}else{
	callback({'response':"User already Registered"});
}});
}


exports.uploadlocation = function(lat,longd,imageID,callback){
  var newGeo = new geo;
  //console.log("GEO "+newGeo)
  geo.lattitude = lat;
  geo.longditude = longd;
  geo.imageID = imageID;

  newGeo.save(function (err, a) {
    if(err){
      callback({'response':"Error"});
    }else {
      console.log(newGeo)
      callback({'response':"Saved"});
    }
  }
);}

// upload a picture to boomerDB
exports.upload = function(fileName,path,lat,longd,callback){

  var newGeo = new geo({
    lattitude: parseFloat(lat), 
    longditude:  parseFloat(longd),
    imageID : fileName });

  var newImage = new img;
  newImage.imageID = fileName;
  newImage.image.data= fs.readFileSync(path);
  newImage.image.contentType = 'image/jpg';
  newImage.save(function (err, a) {
    if(err){
      callback({'response':"Error"});
    }else {
      //console.log(newImage)
      newGeo.save(function (err, a) {
      
        if(err){
          callback({'response':"Error"});
        }else {
          console.log(newGeo)
          callback({'response':"Saved"});
          }
        }
      );
    }
  });
}


// return all users in DB
exports.getusers = function(callback) {
  user.find({},function(err,users){
    callback(users);
});
}


exports.getfriends = function(name, callback) {
  user.find({name:name},function(err,users){
    callback(users.friends);
});
}



exports.removeuser = function(nick,callback) {

user.remove({nick:nick},function(err,users){

	if(!err){

		callback({'response':"Removed Sucessfully"});
	}else{
		callback({'response':"Error"});
	}
});
}

exports.addfriend = function(reg_id,friend,callback) {
 // console.log("searching with: "+reg_id)
 var friendID;
 var friendName;
 fetchId(friend, function(err, user) {
  if (err) {
    console.log(err);
  }
  friendID = user.reg_id
  friendName = user.name
  console.log("friends ID: "+user.reg_id)
  // do something with user
});
  
user.update({reg_id: reg_id},
  {$push: { 'friends' : friend }},{upsert:true}, function(err, data) { 
      callback({'response':'now friends with '+friend});
});


}


function removeUser(arr, val) {
    for(var i=0; i<arr.length; i++) {
        if(arr[i].nick == val) {
            arr.splice(i, 1);
            return arr;
            break;
        }
    }
}

// fetch a reg id based on name
function fetchId(name, callback) {
  user.find({name: name}, function(err, users) {
    if (err) {
      callback(err, null);
    } else {
      callback(null, users[0]);
    }
  });
};

function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 12; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}