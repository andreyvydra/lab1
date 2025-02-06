import diamondImage from '../../static/img/diamond.webp';

export function setDiamondsImages() {
    const diamonds = document.querySelectorAll('.diamond');
    diamonds.forEach((diamond) => {
        diamond.src = url(diamondImage);
        console.log(url(diamondImage))
    });
}
