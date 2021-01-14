package com.google.ar.sceneform.samples.hellosceneform;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class HelloSceneformActivity extends AppCompatActivity {
    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    public String visible = "waterSurface";

    private ArFragment arFragment;
    private ModelRenderable waterSurfaceRenderable;
    public ModelRenderable waterSurfaceAnimationRenderable;
    private int nextAnimation;
    public SkeletonNode waterSurfaceSkeletalNode;
    public TransformableNode transformableNode;
    public TransformableNode mainView1;
    public TransformableNode mainView2;
    public TransformableNode mainView3;
    public TransformableNode mainView4;
    public TransformableNode mainView5;

    public float informationHeight1;
    public float informationHeight2;
    public float informationHeight3;
    public float informationHeight4;
    public float informationHeight5;

    public Anchor anchor;
    public Anchor anchor2;
    public Anchor anchor3;
    public Anchor anchor4;
    public Anchor anchor5;
    public Anchor anchor6;

    public AnchorNode anchorNode;
    public AnchorNode anchorNode2;
    public AnchorNode anchorNode3;
    public AnchorNode anchorNode4;
    public AnchorNode anchorNode5;
    public AnchorNode anchorNode6;

    int progressChangedValue = 10;
    int selected = 0;
    String information1;
    String information2;
    String information3;
    String information4;
    String information5;
    public String time;
    public String waterLevel;
    public String realTimeData;

    public String url;


    public class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        public String doInBackground(Void... params) {
            String title2 ="";
            Document doc;
            try {

                doc = Jsoup.connect("http://www.mysuwanneeriver.org/realtime/river-levels.php").get();
                title2 = doc.title();
                Elements masthead = doc.select("td.val");
                Elements masthead2 = doc.select("td.valEM");
                time = masthead.get(16).text();
                waterLevel = masthead2.get(2).text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title2;
        }

        @Override
        protected void onPostExecute(String result) {
            url = doInBackground();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        new MyTask().execute();


        ModelRenderable.builder()
            .setSource(this, R.raw.water_surface11)
            .build()
            .thenAccept(renderable -> waterSurfaceRenderable = renderable)
            .exceptionally(
                throwable -> {
                  Toast toast =
                      Toast.makeText(this, "Unable to load waterSurfaceSkeletalNode renderable", Toast.LENGTH_LONG);
                  toast.setGravity(Gravity.CENTER, 0, 0);
                  toast.show();
                  return null;
                });

        ModelRenderable.builder()
            .setSource(this, R.raw.water_surface11)
            .build()
            .thenAccept(renderable -> waterSurfaceRenderable = renderable)
            .exceptionally(
                throwable -> {
                    Toast toast =
                        Toast.makeText(this, "Unable to load waterSurfaceSkeletalNode renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });





        arFragment.setOnTapArPlaneListener(
            (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                if (waterSurfaceRenderable == null) {
                    return;
                }
                realTimeData = "The water level at Alapaha is "+waterLevel+" feet at "+time+" EST.";

                // Create the Anchor.
                if(visible == "waterSurface" || selected == 0) {
                    if (anchor == null) {
                        anchor = hitResult.createAnchor();
                        anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());
                        transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                        transformableNode.setParent(anchorNode);
                        // Create the transformable waterSurfaceSkeletalNode and add it to the anchor.
                        //waterSurfaceSkeletalNode = new TransformableNode(arFragment.getTransformationSystem());
                        //waterSurfaceSkeletalNode.setLocalPosition(new Vector3(0.0f, (float) (1.0 * progressChangedValue / 100), 0.0f));
                        waterSurfaceSkeletalNode = new SkeletonNode();
                        waterSurfaceSkeletalNode.setParent(transformableNode);
                        waterSurfaceSkeletalNode.setRenderable(waterSurfaceRenderable);
                        waterSurfaceRenderable.setShadowCaster(false);
                    }
                }

                if(visible == "information") {
                    if(anchor2 == null && selected ==2) {
                        anchor2 = hitResult.createAnchor();
                        anchorNode2 = new AnchorNode(anchor2);
                        anchorNode2.setParent(arFragment.getArSceneView().getScene());
                        mainView1 = new TransformableNode(arFragment.getTransformationSystem());
                        mainView1.setParent(anchorNode2);
                        mainView1.setLocalPosition(new Vector3(0.0f, 0.3f, 0.0f));
                        ViewRenderable.builder()
                            .setView(this, R.layout.info_card_view)
                            .build()
                            .thenAccept(
                                (renderable) -> {
                                    mainView1.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    textView.setText(String.valueOf(information1));
                                })
                            .exceptionally(
                                (throwable) -> {
                                    throw new AssertionError("Could not load plane card view.", throwable);
                                });
                        mainView1.setEnabled(true);
                    }

                    if(anchor3 == null && selected ==3) {
                        anchor3 = hitResult.createAnchor();
                        anchorNode3 = new AnchorNode(anchor3);
                        anchorNode3.setParent(arFragment.getArSceneView().getScene());
                        mainView2 = new TransformableNode(arFragment.getTransformationSystem());
                        mainView2.setParent(anchorNode3);
                        mainView2.setLocalPosition(new Vector3(0.0f, 0.3f, 0.0f));
                        ViewRenderable.builder()
                            .setView(this, R.layout.info_card_view)
                            .build()
                            .thenAccept(
                                (renderable) -> {
                                    mainView2.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    textView.setText(String.valueOf(information2));
                                })
                            .exceptionally(
                                (throwable) -> {
                                    throw new AssertionError("Could not load plane card view.", throwable);
                                });
                        mainView2.setEnabled(true);
                    }

                    if(anchor4 == null && selected ==4) {
                        anchor4 = hitResult.createAnchor();
                        anchorNode4 = new AnchorNode(anchor4);
                        anchorNode4.setParent(arFragment.getArSceneView().getScene());
                        mainView3 = new TransformableNode(arFragment.getTransformationSystem());
                        mainView3.setParent(anchorNode4);
                        mainView3.setLocalPosition(new Vector3(0.0f, 0.3f, 0.0f));
                        ViewRenderable.builder()
                            .setView(this, R.layout.info_card_view)
                            .build()
                            .thenAccept(
                                (renderable) -> {
                                    mainView3.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    textView.setText(String.valueOf(information3));
                                })
                            .exceptionally(
                                (throwable) -> {
                                    throw new AssertionError("Could not load plane card view.", throwable);
                                });
                        mainView3.setEnabled(true);
                    }

                    if(anchor5 == null && selected ==5) {
                        anchor5 = hitResult.createAnchor();
                        anchorNode5 = new AnchorNode(anchor5);
                        anchorNode5.setParent(arFragment.getArSceneView().getScene());

                        mainView4 = new TransformableNode(arFragment.getTransformationSystem());
                        mainView4.setParent(anchorNode5);
                        mainView4.setLocalPosition(new Vector3(0.0f, 0.3f, 0.0f));

                        ViewRenderable.builder()
                            .setView(this, R.layout.info_card_view)
                            .build()
                            .thenAccept(
                                (renderable) -> {
                                    mainView4.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    textView.setText(String.valueOf(information4));
                                })
                            .exceptionally(
                                (throwable) -> {
                                    throw new AssertionError("Could not load plane card view.", throwable);
                                });
                        mainView4.setEnabled(true);
                    }

                    if(anchor6 == null && selected ==6) {
                        anchor6 = hitResult.createAnchor();
                        anchorNode6 = new AnchorNode(anchor6);
                        anchorNode6.setParent(arFragment.getArSceneView().getScene());
                        mainView5 = new TransformableNode(arFragment.getTransformationSystem());
                        mainView5.setParent(anchorNode6);
                        mainView5.setLocalPosition(new Vector3(0.0f, 0.3f, 0.0f));
                        ViewRenderable.builder()
                            .setView(this, R.layout.info_card_view)
                            .build()
                            .thenAccept(
                                (renderable) -> {
                                    mainView5.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    textView.setText(String.valueOf(realTimeData));
                                })
                            .exceptionally(
                                (throwable) -> {
                                    throw new AssertionError("Could not load plane card view.", throwable);
                                });
                        mainView5.setEnabled(true);
                    }
                }
            });


            SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
            seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progressChangedValue = progress;

                        if(selected == 0 && anchor != null) {
                            waterSurfaceSkeletalNode.setLocalPosition(new Vector3(0.0f, (float) (1.0 * progressChangedValue / 100), 0.0f));
                            waterSurfaceSkeletalNode.setParent(transformableNode);
                            //waterSurfaceSkeletalNode.setParent(anchorNode);
                            waterSurfaceSkeletalNode.setRenderable(waterSurfaceRenderable);
                            //waterSurfaceSkeletalNode.select();
                        }

                        if(selected == 2 && anchor2 != null){
                            informationHeight1 = 0.3f + (float) (1.0*progressChangedValue/100);
                            mainView1.setLocalPosition(new Vector3(0.0f, informationHeight1, 0.0f));
                        }

                        if(selected == 3 && anchor3 != null){
                            informationHeight2 = 0.3f + (float) (1.0*progressChangedValue/100);
                            mainView2.setLocalPosition(new Vector3(0.0f, informationHeight2, 0.0f));
                        }

                        if(selected == 4 && anchor4 != null){
                            informationHeight3 = 0.3f + (float) (1.0*progressChangedValue/100);
                            mainView3.setLocalPosition(new Vector3(0.0f, informationHeight3, 0.0f));
                        }

                        if(selected == 5 && anchor5 != null){
                            informationHeight4 = 0.3f + (float) (1.0*progressChangedValue/100);
                            mainView4.setLocalPosition(new Vector3(0.0f, informationHeight4, 0.0f));
                        }

                        if(selected == 6 && anchor6 != null){
                            informationHeight5 = 0.3f + (float) (1.0*progressChangedValue/100);
                            mainView5.setLocalPosition(new Vector3(0.0f, informationHeight5, 0.0f));
                        }
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Toast.makeText( HelloSceneformActivity.this, "Seek bar progress is :" + progressChangedValue,
                                Toast.LENGTH_SHORT).show();
                    }
            });


            new DrawerBuilder().withActivity(this).build();
            //if you want to update the items at a later time it is recommended to keep it in a variable
            PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
            SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings);

            //create the drawer and remember the `Drawer` result object
            Drawer result = new DrawerBuilder()
            .withActivity(this)
            .withTranslucentStatusBar(false)
            .withActionBarDrawerToggle(false)
            .addDrawerItems(
                item1,
                new DividerDrawerItem(),
                item2,
                new SecondaryDrawerItem().withName("Information 2"),
                new SecondaryDrawerItem().withName("Information 3"),
                new SecondaryDrawerItem().withName("Information 4"),
                new SecondaryDrawerItem().withName("Real-time Data")
            )
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    // do something with the clicked item :D
                    selected = position;
                    if(selected == 6){
                        visible = "information";
                    }

                    if( selected > 1 && selected <6){
                        visible = "information";
                        informationDialog(HelloSceneformActivity.this);}

                    if(selected == 0) {
                        waterSurfaceSkeletalNode.setRenderable(waterSurfaceRenderable);
                    }
                    return true;
                }

            })
            .build();
    }


    public void informationDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
        .setTitle("Edit Information")
        .setMessage("Enter the information")
        .setView(taskEditText)
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String task = String.valueOf(taskEditText.getText());
                if(task != null) {
                    if (selected == 2) {
                        information1 = task;
                        if(anchor2 != null){
                            mainView1.setLocalPosition(new Vector3(0.0f, informationHeight1, 0.0f));
                            ViewRenderable.builder()
                                .setView(c, R.layout.info_card_view)
                                .build()
                                .thenAccept(
                                    (renderable) -> {
                                        mainView1.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText(String.valueOf(information1));
                                    })
                                .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });
                            mainView1.setEnabled(true);
                        }
                    }
                    if (selected == 3) {
                        information2 = task;
                        if(anchor3 != null){
                            mainView2.setLocalPosition(new Vector3(0.0f, informationHeight2, 0.0f));
                            ViewRenderable.builder()
                                .setView(c, R.layout.info_card_view)
                                .build()
                                .thenAccept(
                                    (renderable) -> {
                                        mainView2.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText(String.valueOf(information2));
                                    })
                                .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });
                            mainView2.setEnabled(true);
                        }
                    }
                    if (selected == 4) {
                        information3 = task;
                        if(anchor4 != null){
                            mainView1.setLocalPosition(new Vector3(0.0f, informationHeight3, 0.0f));

                            ViewRenderable.builder()
                                .setView(c, R.layout.info_card_view)
                                .build()
                                .thenAccept(
                                    (renderable) -> {
                                        mainView3.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText(String.valueOf(information3));
                                    })
                                .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });
                            mainView3.setEnabled(true);
                        }
                    }
                    if (selected == 5) {
                        information4 = task;
                        if(anchor5 != null){
                            mainView4.setLocalPosition(new Vector3(0.0f, informationHeight4, 0.0f));
                            ViewRenderable.builder()
                                .setView(c, R.layout.info_card_view)
                                .build()
                                .thenAccept(
                                    (renderable) -> {
                                        mainView4.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText(String.valueOf(information4));
                                    })
                                .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });
                            mainView4.setEnabled(true);
                        }
                    }
                    if (selected == 6) {
                        information5 = task;
                        if(anchor6 != null){
                            mainView5.setLocalPosition(new Vector3(0.0f, informationHeight5, 0.0f));
                            ViewRenderable.builder()
                                .setView(c, R.layout.info_card_view)
                                .build()
                                .thenAccept(
                                    (renderable) -> {
                                        mainView5.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText(String.valueOf(information5));
                                    })
                                .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });
                            mainView5.setEnabled(true);
                        }
                    }
                }
            }
        })
        .setNegativeButton("Cancel", null)
        .create();
        dialog.show();
    }


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
}