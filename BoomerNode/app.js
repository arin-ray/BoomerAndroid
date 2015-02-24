
/**
 * Module dependencies.
 */
var express  = require('express');
var connect = require('connect');
var app = express();
var port = process.env.PORT || 8080;

// Configuration
app.use(express.static(__dirname + '/public'));
app.use(connect.logger('dev'));
app.use(connect.bodyParser());

app.use(connect.json());  
app.use(connect.urlencoded());
app.use(connect.cookieParser());


// Routes

require('./routes/routes.js')(app);

app.listen(port);

console.log('running on' + port);
