<%@ page language="java" import="java.util.*,com.teradata.common.cache.CacheUtil" pageEncoding="UTF-8" %>
<%@ page import="net.sf.ehcache.Ehcache" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String cacheNames[] = CacheUtil.getCacheManager().getCacheNames();
    Arrays.sort(cacheNames);
    Map cacheMap = new LinkedHashMap();
    int count = 0;
    long size = 0;
    for (String cacheName : cacheNames) {
        count += CacheUtil.getCache(cacheName).getKeysWithExpiryCheck().size();
        size += CacheUtil.getCache(cacheName).calculateInMemorySize();
        List list = CacheUtil.getCache(cacheName).getKeysWithExpiryCheck();
        Collections.sort(list);
        cacheMap.put(cacheName, list);
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>缓存管理</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
            margin-top: 5px;
        }

        td {
            border: 1px solid grey;
            text-align: center;
            font-size: 14px;
            padding-left: 5px;
            padding-right: 5px;
        }

        button {
            display: inline-block;
            zoom: 1; /* zoom and *display = ie7 hack for display:inline-block */
            *display: inline;
            vertical-align: baseline;
            margin: 0 2px;
            outline: none;
            cursor: pointer;
            text-align: center;
            text-decoration: none;
            font: 11px/100% Arial, Helvetica, sans-serif;
            text-shadow: 0 1px 1px rgba(0, 0, 0, .3);
            padding: 0.35em 1em 0.25em;
            color: #606060;
            border: solid 1px #b7b7b7;
            background: #fff;
        }
    </style>
    <script type="text/javascript" src="js/common.js"/>
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/spin.min.js"></script>
    <script type="text/javascript">
        function checkOn() {
            var checkBoxes = document.getElementsByName("cacheCheckBox");
            var checked = document.getElementById("allCheck").checked;
            for (var i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].checked = checked;
            }
        }

        function searchAndCheckOn(pattern) {
            var checkBoxes = document.getElementsByName("cacheCheckBox");
            var patternItems = null;
            if (pattern != null && pattern.trim() != "")
                patternItems = pattern.split(" ");
            for (var i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].checked = false;
                var match = patternItems != null && patternItems.length > 0;
                for (var j = 0; match && j < patternItems.length; j++) {
                    var patt = new RegExp(patternItems[j], "gi");
                    match = match & (patt.test(checkBoxes[i].getAttribute("ename")));
                }
                if (match)
                    checkBoxes[i].checked = true;
            }
        }

        function cleanCache() {
            if (!confirm('确定要清理所选缓存？'))
                return false;
            showMask();
            var keyStr = "";
            var checkBoxes = document.getElementsByName("cacheCheckBox");
            for (var i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].checked)
                    keyStr += checkBoxes[i].getAttribute("ename") + "@" + checkBoxes[i].getAttribute("key") + "~";
            }
            location.href = "<%=basePath%>servlet/cleanCacheServlet?cacheStr=" + keyStr;
        }

        function reloadConfiguration() {
            location.href = "<%=basePath%>servlet/cleanCacheServlet?cacheStr=application.properties";
        }
    </script>
</head>

<body>
<div>
    <span>已缓存数量：<%=count %>&nbsp;&nbsp;</span>
    <span>内存使用量：<%=size %> byte&nbsp;&nbsp;</span>
    <input type="text" onkeyup="searchAndCheckOn(this.value)">
    <button onclick="showMask();location.reload();">刷新</button>
    <button style="float: right;" onclick="cleanCache();">清理所选缓存</button>
    <button style="float: right;" onclick="reloadConfiguration();">重新加载application.properties</button>
</div>
<table>
    <thead style="background-color: grey;color: white;">
    <td><input type="checkbox" id="allCheck" onchange="checkOn()"></td>
    <td>缓存</td>
    <td colspan="2">元素</td>
    <td>命中次数</td>
    <td>大小（byte）</td>
    <td>失效时间</td>
    </thead>
    <%
        Set keys = cacheMap.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            List list = (List) cacheMap.get(key);
            for (Object o : list) {
                Ehcache cache = CacheUtil.getCache(key);
                String[] str = o.toString().split("\\?");
                StringBuilder url = new StringBuilder();
                if (str[0].indexOf("GET") > -1 || str[0].indexOf("POST") > -1) {
                    url.append(path).append(str[0].substring(str[0].indexOf(":") + 1)).append("?");
                    if (str.length > 1) {
                        url.append(str[1]);
                    }
                    if (url.indexOf("debug") == -1)
                        url.append("&debug=1");
                }
    %>
    <tr>
        <td>
            <input type="checkbox" name="cacheCheckBox" key="<%=key%>"
                   ename="<%=java.net.URLEncoder.encode(o.toString(),"utf-8") %>"/>
        </td>
        <td>
            <%=key%>
        </td>
        <td>
            <%=str[0] %>
        </td>
        <td style="width:50%;word-break:break-all;">
            <%
                String _a = str.length == 1 ? "-" : str[1];
                if (url.length() == 0) {
            %>
            <%=_a%>
            <%} else {%>
            <a style="text-decoration:none;" target="_blank" href="<%=url.toString()%>"><%=_a%>
            </a>
            <%}%>
        </td>
        <td>
            <%=cache.getQuiet(o.toString()).getHitCount() %>
        </td>
        <td>
            <%=cache.getQuiet(o.toString()).getSerializedSize() %>
        </td>
        <td>
            <%=cache.getCacheConfiguration().isEternal() ? "-" : new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cache.getQuiet(o.toString()).getExpirationTime())%>
        </td>
    </tr>
    <% }
    }%>
</table>
</body>
</html>
