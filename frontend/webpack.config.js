const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')


module.exports = {
    entry: {
        index: './js/index.js',
        login: './js/login.js',
        register: './js/register.js',
        objects: './js/objects.js'
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
            template: './html/object-list.html',
            filename: 'object-list.html',
            chunks: ['objects'],
        })
    ]
}
