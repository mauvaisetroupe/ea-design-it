rm -rf ./tmp
mkdir ./tmp
cp ./targer/*original*.jar ./tmp.
cd tmp
unzip *original*
zip -r ../static-web-app.zip static/
cd ..
rm -rf tmp
