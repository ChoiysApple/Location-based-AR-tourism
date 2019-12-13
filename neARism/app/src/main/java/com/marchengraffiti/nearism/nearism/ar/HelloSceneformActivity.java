/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marchengraffiti.nearism.nearism.ar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.marchengraffiti.nearism.nearism.MainActivity;
import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.tflite.ClassifierActivity;

import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity implements View.OnClickListener, Serializable {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;

    private ModelRenderable seoultowerRenderable, artpieceRenderable,
    badgerRenderable, cabinRenderable, carRenderable, sandcastleRenderable,
    iglooRenderable, lampRenderable, mountainRenderable, paperplaneRenderable,
    schoolhouseRenderable, starRenderable, trainRenderable, turtleRenderable,
    shipRenderable, snowmanRenderable, candyRenderable, presentRenderable;

    ImageView seoultower, artpiece, badger, cabin, car, sandcastle,
    igloo, lamp, mountain, paperplane, schoolhouse, star, train, turtle,
    ship, snowman, candy, present;

    View arrayView[];
    int selected = 1;

    private static final String CAPTURE_PATH = "/CAPTURE_TEST";

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        FloatingActionButton photoBtn = findViewById(R.id.photoBtn);
        photoBtn.setOnClickListener(v -> takePhoto());

        seoultower = (ImageView) findViewById(R.id.seoultower);
        artpiece = (ImageView) findViewById(R.id.artpiece);
        badger = (ImageView) findViewById(R.id.badger);
        cabin = (ImageView) findViewById(R.id.cabin);
        car = (ImageView) findViewById(R.id.car);
        sandcastle = (ImageView) findViewById(R.id.sandcastle);
        igloo = (ImageView) findViewById(R.id.igloo);
        lamp = (ImageView) findViewById(R.id.lamp);
        mountain = (ImageView) findViewById(R.id.mountain);
        schoolhouse = (ImageView) findViewById(R.id.schoolhouse);
        paperplane = (ImageView) findViewById(R.id.paperplane);
        star = (ImageView) findViewById(R.id.star);
        train = (ImageView) findViewById(R.id.train);
        turtle = (ImageView) findViewById(R.id.turtle);
        candy = (ImageView) findViewById(R.id.candy);
        present = (ImageView) findViewById(R.id.present);
        ship = (ImageView) findViewById(R.id.ship);
        snowman = (ImageView) findViewById(R.id.snowman);


        /*ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelloSceneformActivity.this, MainActivity.class);
                startActivity(i);
            }
        });*/

        setArrayView();
        setClickListener();

        setupModel();

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    createModel(anchorNode, selected);
                });


//        Intent intent2 = getIntent();
//        String list = intent2.getExtras().getString("results");
//        Log.d("sceneformlist", list);


    }


    private void createModel(AnchorNode anchorNode, int selected) {

        if (selected == 2) {
            TransformableNode seoultower = new TransformableNode(arFragment.getTransformationSystem());
            seoultower.setParent(anchorNode);
            seoultower.setRenderable(seoultowerRenderable);
            seoultower.select();
        }

        if (selected == 3) {
            TransformableNode artpiece = new TransformableNode(arFragment.getTransformationSystem());
            artpiece.setParent(anchorNode);
            artpiece.setRenderable(artpieceRenderable);
            artpiece.select();
        }

        if (selected == 4) {
            TransformableNode badger = new TransformableNode(arFragment.getTransformationSystem());
            badger.setParent(anchorNode);
            badger.setRenderable(badgerRenderable);
            badger.select();
        }

        if (selected == 5) {
            TransformableNode cabin = new TransformableNode(arFragment.getTransformationSystem());
            cabin.setParent(anchorNode);
            cabin.setRenderable(cabinRenderable);
            cabin.select();
        }

        if (selected == 6) {
            TransformableNode car = new TransformableNode(arFragment.getTransformationSystem());
            car.setParent(anchorNode);
            car.setRenderable(carRenderable);
            car.select();
        }

        if (selected == 7) {
            TransformableNode sandcastle = new TransformableNode(arFragment.getTransformationSystem());
            sandcastle.setParent(anchorNode);
            sandcastle.setRenderable(sandcastleRenderable);
            sandcastle.select();
        }

        if (selected == 10) {
            TransformableNode igloo = new TransformableNode(arFragment.getTransformationSystem());
            igloo.setParent(anchorNode);
            igloo.setRenderable(iglooRenderable);
            igloo.select();
        }

        if (selected == 11) {
            TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
            lamp.setParent(anchorNode);
            lamp.setRenderable(lampRenderable);
            lamp.select();
        }

        if (selected == 12) {
            TransformableNode mountain = new TransformableNode(arFragment.getTransformationSystem());
            mountain.setParent(anchorNode);
            mountain.setRenderable(mountainRenderable);
            mountain.select();
        }

        if (selected == 14) {
            TransformableNode schoolhouse = new TransformableNode(arFragment.getTransformationSystem());
            schoolhouse.setParent(anchorNode);
            schoolhouse.setRenderable(schoolhouseRenderable);
            schoolhouse.select();
        }

        if (selected == 15) {
            TransformableNode paperplane = new TransformableNode(arFragment.getTransformationSystem());
            paperplane.setParent(anchorNode);
            paperplane.setRenderable(paperplaneRenderable);
            paperplane.select();
        }

        if (selected == 19) {
            TransformableNode star = new TransformableNode(arFragment.getTransformationSystem());
            star.setParent(anchorNode);
            star.setRenderable(starRenderable);
            star.select();
        }

        if (selected == 21) {
            TransformableNode train = new TransformableNode(arFragment.getTransformationSystem());
            train.setParent(anchorNode);
            train.setRenderable(trainRenderable);
            train.select();
        }

        if (selected == 22) {
            TransformableNode turtle = new TransformableNode(arFragment.getTransformationSystem());
            turtle.setParent(anchorNode);
            turtle.setRenderable(turtleRenderable);
            turtle.select();
        }

        if (selected == 23) {
            TransformableNode candy = new TransformableNode(arFragment.getTransformationSystem());
            candy.setParent(anchorNode);
            candy.setRenderable(candyRenderable);
            candy.select();
        }

        if (selected == 26) {
            TransformableNode present = new TransformableNode(arFragment.getTransformationSystem());
            present.setParent(anchorNode);
            present.setRenderable(presentRenderable);
            present.select();
        }

        if (selected == 27) {
            TransformableNode ship = new TransformableNode(arFragment.getTransformationSystem());
            ship.setParent(anchorNode);
            ship.setRenderable(shipRenderable);
            ship.select();
        }

        if (selected == 28) {
            TransformableNode snowman = new TransformableNode(arFragment.getTransformationSystem());
            snowman.setParent(anchorNode);
            snowman.setRenderable(snowmanRenderable);
            snowman.select();
        }

    }

    private void setupModel() {
        ModelRenderable.builder()
                .setSource(this, R.raw.seoultower)
                .build()
                .thenAccept(renderable -> seoultowerRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load seoultower renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.artpiece)
                .build()
                .thenAccept(renderable -> artpieceRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load artpiece renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.badger)
                .build()
                .thenAccept(renderable -> badgerRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load badger renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.cabin)
                .build()
                .thenAccept(renderable -> cabinRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load cabin renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.car)
                .build()
                .thenAccept(renderable -> carRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load car renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.sandcastle)
                .build()
                .thenAccept(renderable -> sandcastleRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load sandcastle renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        ModelRenderable.builder()
                .setSource(this, R.raw.igloo)
                .build()
                .thenAccept(renderable -> iglooRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load igloo renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.lamp)
                .build()
                .thenAccept(renderable -> lampRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load lamp renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.mountain)
                .build()
                .thenAccept(renderable -> mountainRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load mountain renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.paperplane)
                .build()
                .thenAccept(renderable -> paperplaneRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load paperplane renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        ModelRenderable.builder()
                .setSource(this, R.raw.schoolhouse)
                .build()
                .thenAccept(renderable -> schoolhouseRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load schoolhouse renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.star)
                .build()
                .thenAccept(renderable -> starRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load star renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.train)
                .build()
                .thenAccept(renderable -> trainRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load train renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.turtle)
                .build()
                .thenAccept(renderable -> turtleRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load turtle renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.candy)
                .build()
                .thenAccept(renderable -> candyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load candy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.present)
                .build()
                .thenAccept(renderable -> presentRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load present renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.ship)
                .build()
                .thenAccept(renderable -> shipRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load ship renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.snowman)
                .build()
                .thenAccept(renderable -> snowmanRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load snowman renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private void setClickListener() {
        if(arrayView.length != 0) {
            for (int i = 0; i < arrayView.length; i++) {
                arrayView[i].setOnClickListener(this);
            }
        }
    }

    private void setArrayView() {
        arrayView = new View[]{
                seoultower, artpiece, badger, cabin, car, sandcastle, igloo, lamp, mountain, schoolhouse,
                paperplane, star, train, turtle, candy, ship, present, snowman
        };
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.seoultower)
            selected = 2;
        else if (view.getId() == R.id.artpiece)
            selected = 3;
        else if (view.getId() == R.id.badger)
            selected = 4;
        else if (view.getId() == R.id.cabin)
            selected = 5;
        else if (view.getId() == R.id.car)
            selected = 6;
        else if (view.getId() == R.id.sandcastle)
            selected = 7;
        else if (view.getId() == R.id.igloo)
            selected = 10;
        else if (view.getId() == R.id.lamp)
            selected = 11;
        else if (view.getId() == R.id.mountain)
            selected = 12;
        else if (view.getId() == R.id.schoolhouse)
            selected = 14;
        else if (view.getId() == R.id.paperplane)
            selected = 15;
        else if (view.getId() == R.id.star)
            selected = 19;
        else if (view.getId() == R.id.train)
            selected = 21;
        else if (view.getId() == R.id.turtle)
            selected = 22;
        else if (view.getId() == R.id.candy)
            selected = 23;
        /*else if (view.getId() == R.id.knife)
            selected = 24;
        else if (view.getId() == R.id.machinegun)
            selected = 25;*/
        else if (view.getId() == R.id.present)
            selected = 26;
        else if (view.getId() == R.id.ship)
            selected = 27;
        else if (view.getId() == R.id.snowman)
            selected = 28;
    }

    private String generateFilename() {
        String date =
                new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault()).format(new Date());
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + "Sceneform/" + date + "_screenshot.jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        final String filename = generateFilename();
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(HelloSceneformActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Open in Photos", v -> {
                    File photoFile = new File(filename);

                    Uri photoURI = FileProvider.getUriForFile(HelloSceneformActivity.this,
                            HelloSceneformActivity.this.getPackageName() + ".ar.codelab.name.provider",
                            photoFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                });
                snackbar.show();
            } else {
                Toast toast = Toast.makeText(HelloSceneformActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));


    }
}