/**
 * Created by alex on 14-10-15.
 */
var spinner;

function showMask() {
     var getTarget = function() {
        var target = document.getElementById("spin");
        if (!target) {
            target = document.createElement("div");
            target.setAttribute("class", "mask");
            target.id = "spin";
            document.body.insertBefore(target, document.getElementById("imgDiv"));
        }
        return target;
    }

    if (spinner) {
        spinner.spin(getTarget());
        return;
    }

    var top = window.innerHeight / 2;
    var left = window.innerWidth / 2;
    var opts = {
        lines: 10, // The number of lines to draw
        length: 7, // The length of each line
        width: 4, // The line thickness
        radius: 10, // The radius of the inner circle
        corners: 1, // Corner roundness (0..1)
        rotate: 0, // The rotation offset
        color: '#000', // #rgb or #rrggbb
        speed: 1, // Rounds per second
        trail: 60, // Afterglow percentage
        shadow: false, // Whether to render a shadow
        hwaccel: false, // Whether to use hardware acceleration
        className: 'spinner', // The CSS class to assign to the spinner
        zIndex: 2e9, // The z-index (defaults to 2000000000)
        top: top, // Top position relative to parent in px
        left: left  // Left position relative to parent in px
    };
    spinner = new Spinner(opts);
    spinner.spin(getTarget())
}

function hideMask() {
    document.body.removeChild(document.getElementById("spin"));
    spinner.stop();
}

/**
 * 从url中获取参数值
 * @param url
 * @param name
 * @returns {*}
 */
function getQueryString(url, name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = url.slice(url.indexOf("?")).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}

