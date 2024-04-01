document.querySelector('meta[name=viewport]').setAttribute('content', 'width=device-width,initial-scale=1.0');
var idsToHide = ['top-head', 'top-nav', 'comments-form', 'hd-comments-list', 'hd-comments-navigation', 'footer'];
for (var i = 0; i < idsToHide.length; ++i) {
    var el = document.getElementById(idsToHide[i]);
    if (el) {
        el.style.setProperty('display', 'none', 'important');
    }
}

var images = document.getElementsByTagName('img');
for (var i = 0; i < images.length; i++) {
    var img = images[i];
    if ((img.src.startsWith('https://static.hdrezka.ac/i/') ||
        img.src.startsWith('https://s.cummerata.link/')) &&
        !img.closest('#translators-list')) {
        img.style.display = 'none';
        img.parentElement.parentElement.style.height = '0px';
        img.parentElement.style.height = '0px';
    }
}

var elements = document.querySelectorAll('iframe[src*="s.cummerata.link"]');
for (var i = 0; i < elements.length; i++) {
    elements[i].parentNode.style.display = 'none';
}


var banners = document.querySelectorAll('iframe[src="//s.cummerata.link/"]');
for (var i = 0; i < banners.length; i++) {
    banners[i].style.display = 'none';
    banners.parentElement.parentElement.style.height = '0px';
    banners.parentElement.style.height = '0px';
}

var elem = document.getElementById("simple-episodes-tabs");
if (elem) {
    var nextElem = elem.nextElementSibling;

    while (nextElem) {
        nextElem.style.display = 'none';
        nextElem = nextElem.nextElementSibling;
    }
}

var body = document.getElementsByTagName( "body")[0];
body.style.setProperty("background", 'black')



var classesToHide = ['post__title',
    'b-post__origtitle',
    'b-post__infotable clearfix',
    'b-post__description',
    'b-post__mixedtext',
    'b-post__lastepisodeout',
    'b-post__social_holder_wrapper',
    'b-post__rating_table',
    'b-post__actions',
    'b-sidetitle',
    'b-sidelist__holder',
    'b-post__schedule',
    'b-post__qa_list_block',
    'b-post__mtitle',
    'b-wrapper nopadd',
    'b-post__status_wrapper',
    'b-post__support_holder',
    'b-post__title',
    'b-sidetitle',
    'b-post__partcontent',
    'b-post__infolast'];
for (var i = 0; i < classesToHide.length; ++i) {
    var els = document.getElementsByClassName(classesToHide[i]);
    for (var j = 0; j < els.length; ++j) {
        if (els[j]) {
            els[j].style.setProperty('display', 'none', 'important');
            els[j].parentElement.parentElement.style.height = '0px';
            els[j].parentElement.style.height = '0px';
        }
    }
}

document.body.style.minWidth = 'unset';
document.getElementsByTagName('html')[0].style.height = 'unset';
var translatorArray = document.getElementsByClassName('b-translator__item');
for (var i = 0; i < translatorArray.length; i++) {
    translatorArray[i].style.setProperty('min-width', '100%', 'important');
    translatorArray[i].style.setProperty('width', 'unset', 'important');
}
var translatorBlock = document.getElementsByClassName("b-translators__block");
if (translatorBlock.length > 0) {
    translatorBlock[0].style.setProperty('padding-right', '10px', 'important');
}
var translatorList = document.getElementsByClassName("b-translators__list");
if (translatorList.length > 0) {
    translatorList[0].style.setProperty('padding-right', 'unset', 'important');
}
var playercont = document.getElementById('cdnplayer-container');
if (playercont == null) {
    playercont = document.getElementById('ownplayer');
}
if (playercont != null) {
    playercont.style.setProperty('width', '100%', 'important');
}
var cdnplayer = document.getElementById('cdnplayer');
if (cdnplayer == null) {
    cdnplayer = document.getElementById('videoplayer');
}
if (cdnplayer != null) {
    cdnplayer.style.setProperty('width', '100%', 'important');
}
var restrplayer = document.getElementsByClassName("b-player__restricted");
if (restrplayer.length > 0) {
    if (restrplayer[0]) {
        restrplayer[0].style.setProperty('width', '100%', 'important');
    }
}
var elMain = document.getElementById('main');
if (elMain) {
    elMain.style.setProperty('padding', '0', 'important');
}
var bCont = document.getElementsByClassName('b-container');
if (bCont) {
    bCont[0].style.setProperty('width', 'unset', 'important');
}
var bContCol = document.getElementsByClassName('b-content__columns');
if (bContCol) {
    bContCol[0].style.setProperty('padding', '0', 'important');
    bCont[0].style.setProperty('padding', '0', 'important');
}

// remove advertisements
var bannerCont = document.getElementsByClassName('banner-container')[0];
if (bannerCont) { bannerCont.style.display = 'none'; bannerCont.parentElement.parentElement.style.height = '0px'; }
document.body.classList.remove('active-brand');
function setPos() {
    var isChanged = false;
    for (var i = 0; i < document.body.childNodes.length; i++) {
        var node = document.body.childNodes[i];
        if (node.childNodes.length > 0 && node.style.position == 'fixed') {
            node.style.display = 'none';
            isChanged = true;
            return;
        }
    }
    if (!isChanged) {
        setTimeout(setPos, 1000);
    }
}
setTimeout(setPos, 1000);

var vkGroups = document.getElementById('vk_groups');
if (vkGroups) {
    vkGroups.style.setProperty('display', 'none', 'important');
}

var translationsHint = document.getElementsByClassName('b-rgstats__help')[0];
if (translationsHint) {
    translationsHint.addEventListener('click', function (event) {
        var block = document.getElementsByClassName('tooltipster-base')[0];
        block.style.minWidth = 'unset';
        block.style.maxWidth = 'unset';
        block.style.left = 'unset';
        block.style.width = '100%';
        document.getElementsByClassName('tooltipster-arrow-bottom-right')[0].style.display = 'none';
    });
}

$('#tg-info-block-exclusive-close').click()
$(document).ajaxComplete(function (event, request, settings) {
    Android.updateWatchLater();
});

setTimeout(function () {
    document.body.childNodes[0].style.setProperty('display', 'none', 'important');
    var iframeEls = document.getElementsByTagName('iframe');
    for (var j = 0; j < iframeEls.length; ++j) {
        var iframeEl = iframeEls[j];
        if (iframeEl.id != 'pjsfrrscdnplayer') {
            iframeEl.parentNode.style.setProperty('display', 'none', 'important');
        }
    }

    var aEls = document.getElementsByTagName('a');
    for (var j = 0; j < aEls.length; ++j) {
        var aEl = aEls[j];
        if (aEl.href.match('help')) {
            aEl.style.setProperty('display', 'none', 'important');
            aEl.style.setProperty('height', '0px', 'important');
            aEl.style.setProperty('width', '0px', 'important');
        } else if (aEl.href.match('://')) {
            aEl.parentNode.style.setProperty('display', 'none', 'important');
            aEl.parentNode.style.setProperty('height', '0px', 'important');
            aEl.parentNode.style.setProperty('width', '0px', 'important');
        }
    }
}, 1500)


var mediaElement;
mediaCheck();
document.onclick = function () {
    mediaCheck();
};
function mediaCheck() {
    for (var i = 0; i < document.getElementsByTagName('video').length; i++) {
        var media = document.getElementsByTagName('video')[i];
        media.onplay = function () {
            mediaElement = media;
            JSOUT.mediaAction('true');
        };
        media.onpause = function () {
            mediaElement = media;
            JSOUT.mediaAction('false');
        };
    }
    for (var i = 0; i < document.getElementsByTagName('audio').length; i++) {
        var media = document.getElementsByTagName('audio')[i];
        media.onplay = function () {
            mediaElement = media;
            JSOUT.mediaAction('true');
        };
        media.onpause = function () {
            mediaElement = media;
            JSOUT.mediaAction('false');
        };
    }
}

var mainElements = document.getElementsByClassName("b-content__main")[0].childNodes
for (var i = 0; i < mainElements.length; ++i) {
    var el = mainElements[i];
    if (el.classList && el.classList.length > 0) {
        if (el.classList[0] == "b-post__lastepisodeout") {
            mainElements[i - 6]?.style.setProperty('height', '0px', 'important');
        }

        if (el.classList[0] == "b-post__rating_table") {
            mainElements[i + 2]?.style.setProperty('height', '0px', 'important');
        }
    }
}


const getDocumentId = () => {
    let voiceId = null;
    $('.b-translator__item').each((i, el) => {
        if (el.classList.contains('active')) {
            voiceId = el.getAttribute('data-translator_id');
        }
    });

    let season = null;
    $('.b-simple_season__item').each((i, el) => {
        if (el.classList.contains('active')) {
            season = el.getAttribute('data-tab_id');
        }
    });

    let episode = null;
    $('.b-simple_episode__item').each((i, el) => {
        if (el.classList.contains('active')) {
            episode = el.getAttribute('data-episode_id');
        }
    });

    let titleId = $('.b-userset__fav_holder_data')[0].getAttribute('data-post_id');
    let userId = $('#member_user_id')[0].getAttribute('value');
    if (!userId || userId == '0') {
        throw Error('unauthorized');
    }
    return `${userId}-${titleId}-${voiceId}-${season}-${episode}`;
}


Android.initTime(getDocumentId());
const UPDATE_TIMEOUT = 30000;
const updateTime = (timer) => {
    if (CDNPlayer.api('playing')) {
        Android.updateTime(getDocumentId(), CDNPlayer.api('time'));
    }
    if (timer) {
        setTimeout(() => updateTime(true), UPDATE_TIMEOUT);
    }
};
setTimeout(() => updateTime(true), UPDATE_TIMEOUT);