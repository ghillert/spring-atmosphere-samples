<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
    <head>

        <title>Welcome to Spring Integration</title>

        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

        <link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css'/>" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="<c:url value='/css/blueprint/print.css'/>"  type="text/css" media="print">
        <!--[if lt IE 8]>
            <link rel="stylesheet" href="/css/blueprint/ie.css" type="text/css" media="screen, projection">
        <![endif]-->

        <link rel="stylesheet" href="<c:url value='/css/main.css'/>"  type="text/css">


        <!--[if IE]><script src="<c:url value='/js/excanvas.js'/>"></script><![endif]-->

        <script src="<c:url value='/js/jquery-1.4.2.js'/>"></script>
        <script src="<c:url value='/js/jquery.atmosphere.js'/>"></script>

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
                <h1 class="loud">
                    Welcome to Spring Integration
                </h1>
                <p>
                   This example demonstrates the functionality of the Spring
                   Integration AsyncHttp Adapter. This adapter enables Spring
                   Integration messages to be "pushed" to subscribed browser client.
                   By using the Atmosphere framework, the adapter transparently
                   chooses the optimal transport mechanism based on Browsers and
                   Server capabilities and configurations (e.g. websockets, comet)
                </p>
            </div>
            <div id="content" class="prepend-1 span-17 prepend-top last">
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

                <div id="holder" style="height: 300px;"></div>
            </div>
        </div>

        <script id="template" type="text/x-jquery-tmpl">
        <li><img alt='\${fromUser}' title='\${fromUser}' src='\${profileImageUrl}' width='48' height='48'><div><c:out value='\${text}'/></div></li>
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

                                    asyncHttpStatistics.numberOfTweets = asyncHttpStatistics.numberOfTweets + result.length;

                                    $( "#template" ).tmpl( result ).hide().prependTo( "#twitterMessages").fadeIn();

                                } catch (error) {
                                    asyncHttpStatistics.numberOfErrors++;
                                    console.log("An error ocurred: " + error);
                                }
                            } else {
                                console.log("response.responseBody is null - ignoring.");
                            }
                        }
                    }

                    refresh();
                }

                /* transport can be : long-polling, streaming or websocket */
                $.atmosphere.subscribe("<c:url value='/websockets'/>",
                        !callbackAdded? callback : null,
                $.atmosphere.request = {transport: 'websocket'});
                connectedEndpoint = $.atmosphere.response;
                callbackAdded = true;

            });


        </script>
    </body>
</html>
