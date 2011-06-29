<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
    <head>

        <title>Welcome to Spring Web MVC - Atmosphere Sample</title>

        <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

        <link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css'/>" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="<c:url value='/css/blueprint/print.css'/>"  type="text/css" media="print">
        <!--[if lt IE 8]>
            <link rel="stylesheet" href="/css/blueprint/ie.css" type="text/css" media="screen, projection">
        <![endif]-->

        <link rel="stylesheet" href="<c:url value='/css/main.css'/>"  type="text/css">

        <script src="<c:url value='/js/jquery/jquery-1.4.2.js'/>"></script>
        <script src="<c:url value='/js/jquery/jquery.tmpl.min.js'/>"></script>
        <script src="<c:url value='/js/jquery/jquery.atmosphere.js'/>"></script>


    </head>
    <body>
        <div class="container">
            <div id="header" class="prepend-1 span-22 append-1 last">
                <h1 class="loud">
                    Welcome to Spring Web MVC - Atmosphere Sample
                </h1>
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
            </div>
        </div>

        <script id="template" type="text/x-jquery-tmpl">
        <li><img alt='\${fromUser}' title='\${fromUser}' src='\${profileImageUrl}' width='48' height='48'><div><c:out value='\${text}'/></div></li>
        </script>

        <script type="text/javascript">

            $(function() {

                var asyncHttpStatistics = {
                        transportType: 'N/A',
                        responseState: 'N/A',
                        numberOfCallbackInvocations: 0,
                        numberOfTweets: 0,
                        numberOfErrors: 0
                    };

                var connectedEndpoint;
                var callbackAdded = false;

                function refresh() {

                    console.log("Refreshing data tables...");

                    $('#transportType').html(asyncHttpStatistics.transportType);
                    $('#responseState').html(asyncHttpStatistics.responseState);
                    $('#numberOfCallbackInvocations').html(asyncHttpStatistics.numberOfCallbackInvocations);
                    $('#numberOfTweets').html(asyncHttpStatistics.numberOfTweets);
                    $('#numberOfErrors').html(asyncHttpStatistics.numberOfErrors);

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
                $.atmosphere.subscribe("<c:url value='/websockets/'/>",
                        !callbackAdded? callback : null,
                $.atmosphere.request = {transport: 'websocket'});
                connectedEndpoint = $.atmosphere.response;
                callbackAdded = true;

            });


        </script>
    </body>
</html>
