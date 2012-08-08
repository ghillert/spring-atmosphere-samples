<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>Welcome to Spring Web MVC - Atmosphere Sample</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

		<link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css'/>" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value='/css/blueprint/print.css'/>" type="text/css" media="print">
		<!--[if lt IE 8]>
			<link rel="stylesheet" href="/css/blueprint/ie.css" type="text/css" media="screen, projection">
		<![endif]-->

		<link rel="stylesheet" href="<c:url value='/css/main.css'/>" type="text/css">

		<script src="<c:url value='/js/jquery/jquery.js'/>"></script>
		<script src="<c:url value='/js/jquery/jquery.tmpl.min.js'/>"></script>
		<script src="<c:url value='/js/jquery/jquery.atmosphere.js'/>"></script>

		<script src="<c:url value='/js/underscore.js'/>"></script>
		<script src="<c:url value='/js/handlebars.js'/>"></script>
		<script src="<c:url value='/js/backbone.js'/>"></script>

	</head>
	<body>
		<div class="container">
			<div id="header" class="prepend-1 span-22 append-1 last">
				<h1 class="loud">Welcome to Spring Web MVC - Atmosphere Sample</h1>
				<h4>
					Code: <a href="https://github.com/ghillert/spring-asynchttp-examples">https://github.com/ghillert/spring-atmosphere-samples</a>
				</h4>
			</div>
			<div id="status" class="prepend-1 span-17 last" style="background-color: #FFFFDD;">
				Status Messages will appear here...
			</div>
			<div id="content" class="prepend-1 span-17 prepend-top last">
				<input id="message-field" type="text" size="40" value="Send a tweet to connected clients" /> <input id="message-button" type="button" value="Send" />
				<ul id="twitterMessages">
					<li id="placeHolder">Searching...</li>
				</ul>
			</div>
			<div id="stats" class="prepend-1 span-4 append-1 prepend-top last">
				<table id="asynchHttpStats">
					<caption>AsynchHttp Stats</caption>
					<thead>
						<tr>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>Protocol</td>
							<td id="transportType">N/A</td>
						</tr>
					</tbody>
				</table>
				<table id="chartableStats">
					<caption>Stats</caption>
					<thead>
						<tr>
							<th scope="col"></th>
							<th scope="col"></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row" style="color: #1751A7"># of Messages</th>
							<td id="numberOfCallbackInvocations">0</td>
						</tr>
						<tr>
							<th scope="row" style="color: #8AA717"># Tweets</th>
							<td id="numberOfTweets">0</td>
						</tr>
						<tr>
							<th scope="row" style="color: #A74217"># Errors</th>
							<td id="numberOfErrors">0</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<script id="tweet-template" type="text/x-handlebars-template">
			{{#each tweets}}
				{{#with this}}
					<li>
						<img alt='{{fromUser}}' title='{{fromUser}}' src='{{profileImageUrl}}' width='48' height='48'>
						<div>{{text}}</div>
					</li>
				{{/with}}
			{{/each}}
		</script>

		<script type="text/javascript">

			$(function() {

				if (!window.console) {
					console = {log: function() {}};
				}

				var StatusMessage = Backbone.Model.extend({
				});

				var TwitterMessage = Backbone.Model.extend({
				});

				var TwitterMessages = Backbone.Collection.extend({
					model: TwitterMessage
				});

				var source = $("#tweet-template").html();
				var tweetTemplate = Handlebars.compile(source);

				var asyncHttpStatistics = {
					transportType: 'N/A',
					responseState: 'N/A',
					numberOfTotalMessages: 0,
					numberOfTweets: 0,
					numberOfErrors: 0
				};

				function refresh() {

					console.log("Refreshing data tables...");

					$('#transportType').html(asyncHttpStatistics.transportType);
					$('#responseState').html(asyncHttpStatistics.responseState);
					$('#numberOfCallbackInvocations').html(asyncHttpStatistics.numberOfTotalMessages);
					$('#numberOfTweets').html(asyncHttpStatistics.numberOfTweets);
					$('#numberOfErrors').html(asyncHttpStatistics.numberOfErrors);

				}

				function onMessage(response) {
					asyncHttpStatistics.numberOfTotalMessages++;
					refresh();
					var message = response.responseBody;
					var result;

					try {
						result =  $.parseJSON(message);
					} catch (e) {
						asyncHttpStatistics.numberOfErrors++;
						console.log("An error ocurred while parsing the JSON Data: " + message.data + "; Error: " + e);
						return;
					}

					//asyncHttpStatistics.numberOfCallbackInvocations++;
					//asyncHttpStatistics.transportType = response.transport;
					//asyncHttpStatistics.responseState = response.responseState;

					//$.atmosphere.log('info', ["response.state: " + response.state]);
					//$.atmosphere.log('info', ["response.transport: " + response.transport]);

					var resultType = result['@class'];
					console.log('Object type returned: ' + resultType);

					if (resultType == "org.springframework.mvc.samples.atmosphere.model.TwitterMessage") {
						handleTwitterMessage(new TwitterMessage(result));
					} else if (resultType == "org.springframework.mvc.samples.atmosphere.model.TwitterMessages") {
						console.log('raw-----' + result);
						handleTwitterMessages(new TwitterMessages(result.twitterMessages));
					} else if (resultType == "org.springframework.mvc.samples.atmosphere.model.StatusMessage") {
						handleStatusMessage(new StatusMessage(result));
					} else {
						throw "resultType " + resultType + " is not handled.";
					}

					refresh();
				}

				function handleTwitterMessage(data) {

					console.log("Handling Twitter Message...");

					var visible = $('#placeHolder').is(':visible');

 					if (result.length > 0 && visible) {
						$("#placeHolder").fadeOut();
					}

					//asyncHttpStatistics.numberOfTweets = asyncHttpStatistics.numberOfTweets + result.length;

						/*
						$( "#template" ).tmpl( result ).hide().prependTo( "#twitterMessages").fadeIn(); */
				}

				function handleTwitterMessages(data) {

					console.log("Handling Twitter Messages...");
					var x = data.toJSON();
					console.log(x);

					var visible = $('#placeHolder').is(':visible');

 					if (data.length > 0 && visible) {
						$("#placeHolder").fadeOut();
					}

					asyncHttpStatistics.numberOfTweets = asyncHttpStatistics.numberOfTweets + data.length;

					var context = {
							tweets : data.toJSON()
						};
					var html = tweetTemplate(context);

					$(html).hide().prependTo( "#twitterMessages").fadeIn();

				}

				function handleStatusMessage(data) {
					console.log("Handling Status Message...");
					$('#status').html(data.get('message'));
				}

				var socket = $.atmosphere;
				var subSocket;
				var transport = 'websocket';
				var websocketUrl = "${fn:replace(r.requestURL, r.requestURI, '')}${r.contextPath}/websockets/";

				var request = {
					url: websocketUrl,
					contentType : "application/json",
					logLevel : 'debug',
					//shared : 'true',
					transport : transport ,
					fallbackTransport: 'long-polling',
					//reconnectInterval: 10000,
					//callback: callback,
					onMessage: onMessage,
					onOpen: function(response) {
						console.log('Atmosphere onOpen: Atmosphere connected using ' + response.transport);
						transport = response.transport;
						asyncHttpStatistics.transportType = response.transport;
						refresh();
				    },
					onReconnect: function (request, response) {
						console.log("Atmosphere onReconnect: Reconnecting");
				    },
					onClose: function(response) {
						console.log('Atmosphere onClose executed');
					},

					onError: function(response) {
						console.log('Atmosphere onError: Sorry, but there is some problem with your '
							+ 'socket or the server is down');
					}
				};

				subSocket = socket.subscribe(request);

				$('#message-button').click(function() {
					subSocket.push($('#message-field').val());
				});
			});
		</script>
	</body>
</html>
