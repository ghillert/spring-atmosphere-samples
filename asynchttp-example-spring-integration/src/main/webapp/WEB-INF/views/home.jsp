<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
    <head>

        <title>Spring Integration AsyncHttp Adapter Demo</title>

        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
        <link rel="shortcut icon" href="${ctx}/favicon.ico">
        <link rel="apple-touch-icon" href="${ctx}/apple-touch-icon.png">

        <link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css'/>" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="<c:url value='/css/blueprint/print.css'/>"  type="text/css" media="print">
        <!--[if lt IE 8]>
            <link rel="stylesheet" href="/css/blueprint/ie.css" type="text/css" media="screen, projection">
        <![endif]-->

        <link rel="stylesheet" href="<c:url value='/css/main.css'/>"  type="text/css">

        <!--[if IE]><script src="<c:url value='/js/excanvas.js'/>"></script><![endif]-->

        <script src="<c:url value='/js/jquery-1.4.2.js'/>"></script>
        <script src="<c:url value='/js/jquery.atmosphere.js'/>"></script>
        <script src="<c:url value='/js/jquery-ui-1.8.14.custom.min.js'/>"></script>

        <script src="<c:url value='/js/jquery.tmpl.min.js'/>"></script>

        <script src="<c:url value='/js/raphael/raphael-min.js'/>"   type="text/javascript" charset="utf-8"></script>
        <script src="<c:url value='/js/raphael/g.raphael-min.js'/>" type="text/javascript" charset="utf-8"></script>
        <script src="<c:url value='/js/raphael/g.pie-min.js'/>"     type="text/javascript" charset="utf-8"></script>
        <script src="<c:url value='/js/raphael/g.line-min.js'/>"    type="text/javascript" charset="utf-8"></script>
        <script src="<c:url value='/js/raphael/g.bar-min.js'/>"     type="text/javascript" charset="utf-8"></script>

    </head>
    <body>
        <div class="container">
            <div id="header" class="prepend-1 span-22 append-1 last">
                <h1 class="loud" style="margin-top: 0.5em" class="span-20 last">
                    Spring Integration AsyncHttp Adapter Demo
                </h1>
                <div class="span-17">
	                <p>
	                   This example demonstrates the functionality of the 
	                   <a href="http://www.springsource.org/spring-integration/">Spring Integration</a> 
	                   AsyncHttp Adapter. This adapter enables Spring Integration 
	                   messages to be "pushed" to subscribed browser clients
	                   using the <a href="http://atmosphere.java.net/">Atmosphere framework</a>.
	                </p>
	                <p>
	                   The source code for this example is available via GitHub: 
	                   <a href="https://github.com/ghillert/spring-asynchttp-examples">
	                       https://github.com/ghillert/spring-asynchttp-examples
	                   </a>
	                </p>                
                </div>
                <div id="wallclock" class="span-5 last">
                    <div id="header">AsyncClock</div>
                    <div id="timebox">No time updates.</div>
                </div>
                <div class="span-20 last">
                   <form id="publishMessagesForm">
	                   <label for="publishMessage">Message to Publish:</label>
	                   <input id="publishMessage" name="publishMessage" type="text" size="50"/>
	                   <input id="publishButton"  name="publishButton"  type="submit" value="Publish"/>
	                   <input id="subscribedToTimeService" name="subscribedToTimeService" type="checkbox"/> Subscribed to Time Service?
                   </form>
                </div>
            </div>
            <div id="content" class="prepend-1 span-22 append-1 last">
                <div id="main22" class="span-14 prepend-top append-bottom">
	                <ul id="twitterMessages">
	                    <li id="placeHolder">Searching...</li>
	                </ul>
                </div>
	            <div id="stats" class="span-8 prepend-top append-bottom last">             
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
                                <td>Type</td>
                                <td id="transportType">N/A</td>
                            </tr>
                            <tr>
                                <td># Users</td>
                                <td id="numberOfSubscribers">0</td>
                            </tr>
                            <tr>
                                <td>User Ids</td>
                                <td id="userIds"></td>
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
                                <th scope="row" style="color: #1751A7"># of Callbacks</th>
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
                    <div id="holder" style="height: 180px;"></div>
	            </div>
	        </div>
            <div id="footer" class="prepend-1 span-22 append-1 last">
            <a href="http://www.springsource.org/"><img alt="SpringSource" title="SpringSource" src="${ctx}/images/SpringSource_VMware_HOR_RGB.png"></a>
            </div>
        </div>

        <script id="template" type="text/x-jquery-tmpl">
<li><p id="img-wrapper" style="background-image: url(\${profileImageUrl})"><img alt='\${fromUser}' title='\${fromUser}' src='\${profileImageUrl}' width='48' height='48'/></p><div><c:out value='\${text}'/></div></li>
        </script>
        <script id="userIdTemplate" type="text/x-jquery-tmpl">
            <div>\${}</div>
        </script>
        
        <script type="text/javascript">

            function drawChart(data) {

                $("#holder").empty();
                var r = Raphael("holder");
                r.clear();

                fin = function () {
                    this.flag = r.g.popup(this.bar.x, this.bar.y, this.bar.value || "0").insertBefore(this);
                },
                fout = function () {
                    this.flag.animate({opacity: 0}, 300, function () {this.remove();});
                },
                fin2 = function () {
                    var y = [], res = [];
                    for (var i = this.bars.length; i--;) {
                        y.push(this.bars[i].y);
                        res.push(this.bars[i].value || "0");
                    }
                    this.flag = r.g.popup(this.bars[0].x, Math.min.apply(Math, y), res.join(", ")).insertBefore(this);
                },
                fout2 = function () {
                    this.flag.animate({opacity: 0}, 300, function () {this.remove();});
                };
                r.g.txtattr.font = "12px 'Fontin Sans', Fontin-Sans, sans-serif";

                r.g.text(100, 10, "Stats");

                r.g.barchart(5, 15, 150, 220, data, {stacked: true}).hover(fin, fout);

            }

            $(function() {

            	if (!window.console) {
            		window.console = {log: function() {  }};
            	}

                var asyncHttpStatistics = {
                        transportType: 'N/A',
                        responseState: 'N/A',
                        numberOfCallbackInvocations: 0,
                        numberOfTweets: 0,
                        numberOfErrors: 0
                    };

            drawChart([[asyncHttpStatistics.numberOfCallbackInvocations, 0, 0], [0,asyncHttpStatistics.numberOfTweets,0], [0,0,asyncHttpStatistics.numberOfErrors]]);

                var connectedEndpoint;
                var callbackAdded = false;

                function refresh() {

                    console.log("Refreshing data tables...");

                    $('#transportType').html(asyncHttpStatistics.transportType);
                    $('#responseState').html(asyncHttpStatistics.responseState);
                    $('#numberOfCallbackInvocations').html(asyncHttpStatistics.numberOfCallbackInvocations);
                    $('#numberOfTweets').html(asyncHttpStatistics.numberOfTweets);
                    $('#numberOfErrors').html(asyncHttpStatistics.numberOfErrors);
                    drawChart([[asyncHttpStatistics.numberOfCallbackInvocations, 0, 0], [0,asyncHttpStatistics.numberOfTweets,0], [0,0,asyncHttpStatistics.numberOfErrors]]);
                    }

                function callback(response)
                {
                    asyncHttpStatistics.numberOfCallbackInvocations++;
                    asyncHttpStatistics.transportType = response.transport;
                    asyncHttpStatistics.responseState = response.responseState;

                    $.atmosphere.log('info', ["response.state: " + response.state]);
                    $.atmosphere.log('info', ["response.transport: " + response.transport]);
                    if (response.transport != 'polling' && response.state != 'connected' && response.state != 'closed') {
                        $.atmosphere.log('info', ["response.responseBody: " + response.responseBody]);
                        if (response.status == 200) {
                            var data = response.responseBody;

                            if (data) {

                                try {
                                    var result =  $.parseJSON(data);

                                    var visible = $('#placeHolder').is(':visible');

                                    if (result.length > 0 && visible) {
                                        $("#placeHolder").fadeOut();
                                    }

                                    if (result[0].type == 'StatisticMessage') {
                                    	console.dir(result[0]);
                                        $('#numberOfSubscribers').html(result[0].numberOfSubscribers);
                                        $("#userIds").empty().prepend($( "#userIdTemplate" ).tmpl( result[0].subscriberIds));   
                                         
                                    } else if (result[0].type == 'TimeMessage') {
                                    	
                                        $('#timebox').html(result[0].time);
                                        $('#timebox').effect("highlight", {}, 3000);     
                                    	
                                    } else if (result[0].type == 'TwitterMessage') {
                                        asyncHttpStatistics.numberOfTweets = asyncHttpStatistics.numberOfTweets + result.length;

                                        $( "#template" ).tmpl( result ).hide().prependTo( "#twitterMessages").fadeIn();
                                    } else {
                                    	console.log("Usupported object type: " + result[0].type);
                                    }

                                } catch (error) {
                                    asyncHttpStatistics.numberOfErrors++;
                                    console.log("An error ocurred: " + error);
                                }
                            } else {
                                console.log("response.responseBody is null - ignoring.");
                            }
                        }
                    }

                    $("#twitterMessages li").slice(10).remove();
                    
                    refresh();
                }

                /* transport can be : long-polling, streaming or websocket */
                $.atmosphere.subscribe("${fn:replace(r.requestURL, r.requestURI, '')}${r.contextPath}/websockets",
                        !callbackAdded? callback : null,
                $.atmosphere.request = {transport: 'websocket'});
                connectedEndpoint = $.atmosphere.response;
                callbackAdded = true;

                $("#subscribedToTimeService").click(function(data) {

                    $.ajax({
                        type: 'GET',
                        url: "${fn:replace(r.requestURL, r.requestURI, '')}${r.contextPath}/timeservice?subscribe=" + $(data.target).is(':checked'),
                        success: function(data){
                            $("#timebox").html(data);
                          }
                      });
                	
                });
                
                $("#publishMessagesForm").submit(function() {
                	
                	var spinner = $("<img src='images/ajax-loader.gif' />").insertAfter("#publishButton");
                	$("#publishButton").attr('disabled', 'disabled');
                	
                    var twitterMessage = {'id':0,
                    		              'text':'',
                    		              'createdAt':1309887977000,
                    		              'fromUser' :'BROADCAST',
                    		              'profileImageUrl':''};
  	
                    twitterMessage.text = $('#publishMessage').val();
                    
                    $('#publishMessage').val("");
                    
                    console.log("Submitting Ajax form: " + twitterMessage);

                    $.ajax({
                    	  type: 'POST',
                    	  url: "${fn:replace(r.requestURL, r.requestURI, '')}${r.contextPath}/websockets",
                    	  data: JSON.stringify(twitterMessage),
                    	  success: successAsyncHttpCall,
                    	  error:   function(data) { console.log(data); spinner.remove(); $("#publishButton").removeAttr('disabled'); alert("Something went wrong: " + data.statusText + "("+ data.status +")"); },
                    	  dataType: "json",
                    	  contentType: "application/json"
                    	});

                    function successAsyncHttpCall(data) {
                    	console.log(data); 
                    	spinner.remove(); 
                    	$("#publishButton").removeAttr('disabled');
                    }
                    
                    
                	return false;
                	  
                });
                
            });

        </script>
    </body>
</html>
