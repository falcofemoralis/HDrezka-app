XMLHttpRequest.prototype.open = (function (open) {
    return function (method, url, async) {
        if (url.match(/franecki.net/g) || url.match(/biocdn.net/g) || url.match(/franeski.net/g) || url.match(/reichelcormier.bid/g) || url.match(/track.adpod.in.bid/g)) {
            console.log('blocked');
        } else {
            open.apply(this, arguments);
        }
    };
})(XMLHttpRequest.prototype.open);