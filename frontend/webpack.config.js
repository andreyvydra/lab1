const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')


module.exports = {
    entry: {
        index: './js/index.js',
        login: './js/login.js',
        register: './js/register.js',
        location: './js/location.js',
        person: './js/person.js',
        dragon: './js/dragon.js',
        dragonCave: './js/dragonCave.js',
        dragonHead: './js/dragonHead.js',
        coordinates: './js/coordinates.js'
    },
    output: {
        filename: '[name].js',
        path: path.resolve(__dirname, 'dist'),
        clean: true
    },
    module: {
        rules: [
            { test: /\.mp3$/, loader: "file-loader"},
            { test: /\.css$/, use: [ 'style-loader', 'css-loader' ] },
            {
                test: /\.(png|jpg|jpeg|webp|gif|svg)$/,
                type: 'asset/resource',
                generator: {
                    filename: 'static/img/[name][ext]',
                },
            },
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './html/index.html',
            filename: 'index.html',
            chunks: ['index'],
        }),
        new HtmlWebpackPlugin({
            template: './html/register.html',
            filename: 'register.html',
            chunks: ['register'],
        }),
        new HtmlWebpackPlugin({
            template: './html/login.html',
            filename: 'login.html',
            chunks: ['login'],
        }),
        new HtmlWebpackPlugin({
            template: './html/location.html',
            filename: 'location.html',
            chunks: ['location'],
        }),
        new HtmlWebpackPlugin({
            template: './html/person.html',
            filename: 'person.html',
            chunks: ['person'],
        }),
        new HtmlWebpackPlugin({
            template: './html/dragon.html',
            filename: 'dragon.html',
            chunks: ['dragon'],
        }),
        new HtmlWebpackPlugin({
            template: './html/dragon_cave.html',
            filename: 'dragon_cave.html',
            chunks: ['dragonCave'],
        }),
        new HtmlWebpackPlugin({
            template: './html/dragon_head.html',
            filename: 'dragon_head.html',
            chunks: ['dragonHead'],
        }),
        new HtmlWebpackPlugin({
            template: './html/coordinates.html',
            filename: 'coordinates.html',
            chunks: ['coordinates'],
        })
    ]
}
