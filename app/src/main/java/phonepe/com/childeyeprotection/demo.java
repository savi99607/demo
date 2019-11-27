//package phonepe.com.childeyeprotection;
//
//import java.util.ArrayList;
//
//public class demo {
//}
//    private void addCateItems() {
//        ArrayList<Object> lion = new ArrayList<>();
//        lion.add(R.mipmap.lion);
//        lion.add(R.mipmap.lion1);
//        lion.add("Lion");
//        cateAr.put(0, lion);
//
//
//        ArrayList<Object> tiger = new ArrayList<>();
//        tiger.add(R.mipmap.tiger);
//        tiger.add(R.mipmap.tiger1);
//        tiger.add("Tiger");
//        cateAr.put(1, tiger);
//
//
//        ArrayList<Object> elephant = new ArrayList<>();
//        elephant.add(R.mipmap.elephant);
//        elephant.add(R.mipmap.elephant1);
//        elephant.add("elephant");
//        cateAr.put(2, elephant);
//
//        ArrayList<Object> monkey = new ArrayList<>();
//        monkey.add(R.mipmap.monkey);
//        monkey.add(R.mipmap.monkey1);
//        monkey.add("Monkey");
//        cateAr.put(3, monkey);
//
//
//        ArrayList<Object> giraffe = new ArrayList<>();
//        giraffe.add(R.mipmap.giraffe);
//        giraffe.add(R.mipmap.giraffe1);
//        giraffe.add("Giraffe");
//        cateAr.put(4, giraffe);
//
//        ArrayList<Object> camel = new ArrayList<>();
//        camel.add(R.mipmap.camel);
//        camel.add(R.mipmap.camel1);
//        camel.add("Giraffe");
//        cateAr.put(5, camel);
//
//
//        ArrayList<Object> alligator = new ArrayList<>();
//        alligator.add(R.mipmap.alligator);
//        alligator.add(R.mipmap.alligator1);
//        alligator.add("Alligator");
//        cateAr.put(6, alligator);
//
//        ArrayList<Object> deer = new ArrayList<>();
//        deer.add(R.mipmap.deer);
//        deer.add(R.mipmap.deer1);
//        deer.add("deer");
//        cateAr.put(7, deer);
//
//
//        ArrayList<Object> fish = new ArrayList<>();
//        fish.add(R.mipmap.fish);
//        fish.add(R.mipmap.fish1);
//        fish.add("Fish");
//        cateAr.put(9, fish);
//
//
//        ArrayList<Object> turtle = new ArrayList<>();
//        turtle.add(R.mipmap.turtle);
//        turtle.add(R.mipmap.turtle1);
//        turtle.add("Turtle");
//        cateAr.put(10, turtle);
//
//        ArrayList<Object> parrot = new ArrayList<>();
//        parrot.add(R.mipmap.parrot);
//        parrot.add(R.mipmap.parrot1);
//        parrot.add("Parrot");
//        cateAr.put(11, parrot);
//
//        ArrayList<Object> swan = new ArrayList<>();
//        swan.add(R.mipmap.swan);
//        swan.add(R.mipmap.swan1);
//        swan.add("Swan");
//        cateAr.put(12, swan);
//
//        ArrayList<Object> pigeon = new ArrayList<>();
//        pigeon.add(R.mipmap.pigeon);
//        pigeon.add(R.mipmap.pigeon1);
//        pigeon.add("Pigeon");
//        cateAr.put(13, pigeon);
//
//        ArrayList<Object> owl = new ArrayList<>();
//        owl.add(R.mipmap.owl);
//        owl.add(R.mipmap.owl1);
//        owl.add("Owl");
//        cateAr.put(14, owl);
//
//        ArrayList<Object> hen = new ArrayList<>();
//        hen.add(R.mipmap.hen);
//        hen.add(R.mipmap.hen1);
//        hen.add("Hen");
//        cateAr.put(15, hen);
//
//    }



//    private void showSystemAlert()
//    {
//        ArrayList<Object> imagesObj;
//        if(count<cateAr.size())
//        {
//            imagesObj = cateAr.get(count);
//
//            final WindowManager manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.gravity = Gravity.CENTER;
//            if (Build.VERSION.SDK_INT >= 23) {
//                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            } else {
//                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//            }
//            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//            layoutParams.alpha = 1.0f;
//            layoutParams.packageName = getPackageName();
//            layoutParams.buttonBrightness = 1f;
//            layoutParams.windowAnimations = android.R.style.Animation_Dialog;
//
//            final View view = View.inflate(getApplicationContext(), R.layout.alertdialog, null);
//            ImageView image1 = (ImageView) view.findViewById(R.id.image1);
//            ImageView image2 = (ImageView) view.findViewById(R.id.image2);
//            TextView itemname = (TextView) view.findViewById(R.id.itemname);
//
//
//            image1.setImageResource(Integer.parseInt(imagesObj.get(0).toString()));
//            image2.setImageResource(Integer.parseInt(imagesObj.get(1).toString()));
//            itemname.setText(imagesObj.get(2).toString());
//
//
//            image1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    manager.removeView(view);
//                }
//            });
//
//            image2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    manager.removeView(view);
//                }
//            });
//
//
//            manager.addView(view, layoutParams);
//
//        }
//        else
//        {
//            count=-1;
//        }
//    }
