const getData = () => {
    let voiceId = null
    $('.b-translator__item').each((i, el) => {
        if (el.classList.contains('active')) {
            voiceId = el.getAttribute('data-translator_id');
        }
    });

    let season = null
    $('.b-simple_season__item').each((i, el) => {
        if (el.classList.contains('active')) {
            season = el.getAttribute('data-tab_id');
        }
    });


    let episode = null
    $('.b-simple_episode__item').each((i, el) => {
        if (el.classList.contains('active')) {
            episode = el.getAttribute('data-episode_id');
        }
    });

    let titleId = $('.b-userset__fav_holder_data')[0].getAttribute('data-post_id')

    return { voiceId, season, episode, titleId };
}

window.onload = () => {
    window.postMessage({ action: "get", data: { ...getData() } })
}

window.addEventListener('message', (e) => {
    if (e.data.action == 'seek') {
        CDNPlayer.api('seek', e.data.data.time)
    } else if (e.data.action == 'fetched') {
        $(document).ajaxComplete(() => window.postMessage({ action: "update", data: { ...getData(), time: CDNPlayer.api('time') } }));
    }
});