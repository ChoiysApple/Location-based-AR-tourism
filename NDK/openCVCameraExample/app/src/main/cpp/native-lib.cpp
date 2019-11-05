#include <jni.h>
#include <opencv2/opencv.hpp>


using namespace cv;
extern "C"
JNIEXPORT void JNICALL
Java_com_gachon_opencvcameraexample_MainActivity_imageprocessing(JNIEnv *env, jobject instance,
                                                                 jlong inputImage,
                                                                 jlong outputImage, jint th1,
                                                                 jint th2) {

    Mat &img_input = *(Mat *) inputImage;

    Mat &img_output = *(Mat *) outputImage;



    cvtColor( img_input, img_output, COLOR_RGB2GRAY);


    blur( img_output, img_output, Size(5,5) );

    Canny( img_output, img_output, th1, th2);

}