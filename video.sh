cd Documents/Docs/High\ School/Coding/Java/Professional/Grad/SlideshowMaker/OUTPUT
VAL=0
for file in ./* ;
do
  cd "OUT$(printf "%03d" $VAL)"
  ffmpeg -probesize 2147483647 -analyzeduration 2147483647 -r 30 -f image2 -s 1920x1080 -pattern_type glob -i "*.PNG" -vcodec libx264 -crf 15  -pix_fmt yuv420p ~/Desktop/TEMP/"test$(printf "%03d" $VAL).mp4"
  cd ../
  VAL=$((VAL + 1))
done
