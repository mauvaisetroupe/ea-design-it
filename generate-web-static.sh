rm -rf ./tmp
mkdir ./tmp
cp ./target/*original ./tmp/ea-design-it.zip
cd tmp
unzip ea-design-it.zip
zip -r ../static-web-app.zip static/
cd ..
rm -rf tmp
