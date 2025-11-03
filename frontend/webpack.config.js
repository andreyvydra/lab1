const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')


module.exports = {
    entry: {
        index: './js/index.js',
        login: './js/login.js',
        register: './js/register.js',
        location: './js/location.js',
        person: './js/person.js',
        product: './js/product.js',
        organization: './js/organization.js',
        address: './js/address.js',
        coordinates: './js/coordinates.js',
        specialActions: './js/specialActions.js',
        adminRequests: './js/adminRequests.js',
        importHistory: './js/importHistory.js'
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
            template: './html/product.html',
            filename: 'product.html',
            chunks: ['product'],
        }),
        new HtmlWebpackPlugin({
            template: './html/organization.html',
            filename: 'organization.html',
            chunks: ['organization'],
        }),
        new HtmlWebpackPlugin({
            template: './html/address.html',
            filename: 'address.html',
            chunks: ['address'],
        }),
        new HtmlWebpackPlugin({
            template: './html/coordinates.html',
            filename: 'coordinates.html',
            chunks: ['coordinates'],
        }),
        new HtmlWebpackPlugin({
            template: './html/forms/product_form.html',
            filename: './forms/product_form.html',
            chunks: []
        }),
        new HtmlWebpackPlugin({
            template: './html/forms/coordinates_form.html',
            filename: './forms/coordinates_form.html',
            chunks: []
        }),
        
        new HtmlWebpackPlugin({
            template: './html/forms/location_form.html',
            filename: './forms/location_form.html',
            chunks: []
        }),
        new HtmlWebpackPlugin({
            template: './html/forms/person_form.html',
            filename: './forms/person_form.html',
            chunks: []
        }),
        new HtmlWebpackPlugin({
            template: './html/forms/organization_form.html',
            filename: './forms/organization_form.html',
            chunks: []
        }),
        new HtmlWebpackPlugin({
            template: './html/forms/address_form.html',
            filename: './forms/address_form.html',
            chunks: []
        }),
        new HtmlWebpackPlugin({
            template: './html/admin_requests.html',
            filename: './admin_requests.html',
            chunks: ['adminRequests']
        }),
        new HtmlWebpackPlugin({
            template: './html/import_history.html',
            filename: './import_history.html',
            chunks: ['importHistory']
        }),
        new HtmlWebpackPlugin({
            template: './html/special_actions.html',
            filename: './special_actions.html',
            chunks: ['specialActions']
        })
    ]
}
