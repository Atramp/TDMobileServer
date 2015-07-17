<!DOCTYPE html>
<html manifest="index.appcache">
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title></title>
    <script src="pictures.js?v=${version}"></script>
    <script>
        // Check if a new cache is available on page load.
        if (window.applicationCache) {
            window.applicationCache.addEventListener('updateready', function (e) {
                if (window.applicationCache.status == window.applicationCache.UPDATEREADY) {
                    window.location.reload();
                }
            }, false);
        }
    </script>
    <style>
        body, div {
            margin: 0;
            padding: 0;
        }
    </style>
</head>
<body>
<iframe src="main.html" style="height: 100vh;width:100vw;" frameborder="0"></iframe>
</body>
</html>