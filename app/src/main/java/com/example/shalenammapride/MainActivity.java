package com.example.shalenammapride;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int PAPER = Color.rgb(244, 241, 237);
    private static final int INK = Color.rgb(26, 26, 26);
    private static final int ACCENT = Color.rgb(230, 126, 34);

    private FrameLayout contentFrame;
    private LinearLayout bottomNav;
    private EditText usernameInput;
    private EditText passwordInput;
    private boolean kannada;
    private String activeTab = "home";
    private String previousTab = "home";
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(buildLoginScreen());
    }

    private View buildLoginScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(22), dp(22), dp(22), dp(22));
        root.setBackgroundColor(PAPER);

        LinearLayout card = borderedBox();
        card.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.ic_launcher);
        card.addView(logo, fixed(dp(92), dp(92)));

        TextView title = title("Shale-Namma Pride", 32);
        title.setGravity(Gravity.CENTER);
        card.addView(title);

        TextView tag = text("Building trust through transparency", 15, false);
        tag.setGravity(Gravity.CENTER);
        card.addView(tag);

        usernameInput = input("Username");
        usernameInput.setSingleLine(true);
        card.addView(usernameInput);

        passwordInput = input("Password");
        passwordInput.setSingleLine(true);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        card.addView(passwordInput);

        Button login = button("Login", true);
        login.setOnClickListener(v -> login());
        card.addView(login);

        TextView hint = small("Enter any name. Demo password: 1234");
        hint.setGravity(Gravity.CENTER);
        card.addView(hint);

        root.addView(card, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return root;
    }

    private void login() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Enter username");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Enter password");
            return;
        }
        if ("1234".equals(password)) {
            loggedIn = true;
            activeTab = "home";
            previousTab = "home";
            setContentView(buildAppShell());
            showTab("home");
            Toast.makeText(this, "Welcome, " + username, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Use demo password: 1234", Toast.LENGTH_LONG).show();
    }

    private View buildAppShell() {
        LinearLayout shell = new LinearLayout(this);
        shell.setOrientation(LinearLayout.VERTICAL);
        shell.setBackgroundColor(PAPER);

        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER_VERTICAL);
        nav.setPadding(dp(16), dp(14), dp(16), dp(14));
        nav.setBackgroundColor(Color.WHITE);

        Button back = button(kannada ? kn("Back") : "Back", false);
        back.setOnClickListener(v -> goBack());
        nav.addView(back, new LinearLayout.LayoutParams(dp(86), ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView appName = title(kannada ? kn("Shale-Namma Hemme") : "Shale-Namma Pride", 24);
        nav.addView(appName, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        Button lang = button(kannada ? "English" : "Kannada", false);
        lang.setOnClickListener(v -> {
            kannada = !kannada;
            setContentView(buildAppShell());
            showTab(activeTab);
        });
        nav.addView(lang, new LinearLayout.LayoutParams(dp(112), ViewGroup.LayoutParams.WRAP_CONTENT));
        shell.addView(nav);

        contentFrame = new FrameLayout(this);
        shell.addView(contentFrame, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1
        ));

        bottomNav = new LinearLayout(this);
        bottomNav.setGravity(Gravity.CENTER);
        bottomNav.setPadding(dp(4), dp(6), dp(4), dp(6));
        bottomNav.setBackgroundColor(Color.WHITE);
        shell.addView(bottomNav);
        buildBottomNav();

        return shell;
    }

    private void buildBottomNav() {
        bottomNav.removeAllViews();
        addNavButton("home", kannada ? kn("Home") : "Home");
        addNavButton("meals", kannada ? kn("Meals") : "Meals");
        addNavButton("facilities", kannada ? kn("Tour") : "Tour");
        addNavButton("stars", kannada ? kn("Stars") : "Stars");
        addNavButton("feedback", kannada ? kn("Feedback") : "Feedback");
    }

    private void addNavButton(String tab, String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setAllCaps(false);
        button.setTextSize(11);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setTextColor(tab.equals(activeTab) ? Color.WHITE : INK);
        button.setBackgroundColor(tab.equals(activeTab) ? INK : Color.WHITE);
        button.setOnClickListener(v -> showTab(tab));
        bottomNav.addView(button, new LinearLayout.LayoutParams(0, dp(54), 1));
    }

    private void showTab(String tab) {
        if (!tab.equals(activeTab)) {
            previousTab = activeTab;
        }
        activeTab = tab;
        if (contentFrame == null) return;
        contentFrame.removeAllViews();
        if ("meals".equals(tab)) contentFrame.addView(mealsScreen());
        else if ("facilities".equals(tab)) contentFrame.addView(facilityScreen());
        else if ("stars".equals(tab)) contentFrame.addView(starsScreen());
        else if ("feedback".equals(tab)) contentFrame.addView(feedbackScreen());
        else contentFrame.addView(homeScreen());
        buildBottomNav();
    }

    private void goBack() {
        if (!loggedIn) {
            finish();
            return;
        }
        if (!"home".equals(activeTab)) {
            showTab("home");
            return;
        }
        loggedIn = false;
        activeTab = "home";
        previousTab = "home";
        setContentView(buildLoginScreen());
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private View homeScreen() {
        LinearLayout page = page();
        page.addView(editorialLabel("Volume 01 / Issue 04"));

        LinearLayout hero = borderedBox();
        hero.addView(title(kannada ? kn("Shale-Namma") : "Shale-Namma", 42));
        hero.addView(title(kannada ? kn("Modern School Portal") : "Modern School Portal", 22));
        hero.addView(text(kannada ? kn("Transparency creates trust in our school community.")
                : "Building trust through transparency.", 15, false));
        Button tour = button(kannada ? kn("Open Facility Tour") : "Open Facility Tour", true);
        tour.setOnClickListener(v -> showTab("facilities"));
        hero.addView(tour);
        page.addView(hero);

        LinearLayout grid = new LinearLayout(this);
        grid.setOrientation(LinearLayout.HORIZONTAL);
        grid.addView(featureTile(kannada ? kn("Daily Meals") : "Daily Meals",
                kannada ? kn("Bulletin / 2h ago") : "Bulletin / 2h ago", "meals"), weightBox());
        grid.addView(featureTile(kannada ? kn("Student Stars") : "Student Stars",
                kannada ? kn("Special Report") : "Special Report", "stars"), weightBox());
        page.addView(grid);

        page.addView(sectionHeading(kannada ? kn("Today's Mid-day Meal") : "Today's Mid-day Meal"));
        page.addView(newsCard(kannada ? kn("Daily Highlight") : "Daily Highlight",
                kannada ? kn("Nutrition Report: Rice, Sambar and Protein Egg") : "Nutrition Report: Rice, Sambar and Protein Egg",
                kannada ? kn("Quality verified meal served with care.") : "Quality verified meal served with care.",
                R.drawable.midday_meal_hd));

        page.addView(sectionHeading(kannada ? kn("Quick Facility Tour") : "Quick Facility Tour"));
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(miniCard(kannada ? kn("Science Lab") : "Science Lab"), weightBox());
        row.addView(miniCard(kannada ? kn("Library") : "Library"), weightBox());
        row.addView(miniCard(kannada ? kn("Sports Room") : "Sports Room"), weightBox());
        page.addView(row);
        return wrap(page);
    }

    private View mealsScreen() {
        LinearLayout page = page();
        page.addView(bigHeader(kannada ? kn("Daily Meals") : "Daily Meals",
                kannada ? kn("A nutritious meal served with care.") : "A nutritious meal served with care."));
        page.addView(mealCard(kannada ? kn("Today") : "Today", kannada ? kn("Rice, Sambar, Egg, Milk") : "Rice, Sambar, Egg, Milk"));
        page.addView(mealCard(kannada ? kn("Yesterday") : "Yesterday", kannada ? kn("Vegetable Pulav with Raita") : "Vegetable Pulav with Raita"));
        page.addView(mealCard("02 May", kannada ? kn("Dal Khichdi and Curd") : "Dal Khichdi and Curd"));
        return wrap(page);
    }

    private View facilityScreen() {
        LinearLayout page = page();
        page.addView(bigHeader(kannada ? kn("Facility Tour") : "Facility Tour",
                kannada ? kn("School facility gallery") : "School facility gallery"));
        page.addView(facilityCard(kannada ? kn("Smart Classroom") : "Smart Classroom",
                kannada ? kn("Equipped with digital boards and educational software.") : "Equipped with digital boards and educational software."));
        page.addView(facilityCard(kannada ? kn("Library") : "Library",
                kannada ? kn("Over 5,000 books available for students and community.") : "Over 5,000 books available for students and community."));
        page.addView(facilityCard(kannada ? kn("Hygiene Toilets") : "Hygiene Toilets",
                kannada ? kn("Clean facilities with running water.") : "Clean facilities with running water."));
        page.addView(facilityCard(kannada ? kn("Science Lab") : "Science Lab",
                kannada ? kn("Modern equipment for hands-on experiments.") : "Modern equipment for hands-on experiments."));
        return wrap(page);
    }

    private View starsScreen() {
        LinearLayout page = page();
        page.addView(bigHeader(kannada ? kn("Student Stars") : "Student Stars",
                kannada ? kn("All student stars") : "All student stars"));
        page.addView(starCard(kannada ? kn("Rahul Patil") : "Rahul Patil",
                kannada ? kn("Student of the Week") : "Student of the Week",
                kannada ? kn("100% Attendance for 3 months") : "100% Attendance for 3 months"));
        page.addView(starCard(kannada ? kn("Priya Hegde") : "Priya Hegde",
                kannada ? kn("Sports Winner") : "Sports Winner",
                kannada ? kn("1st Prize in State Level Running") : "1st Prize in State Level Running"));
        return wrap(page);
    }

    private View feedbackScreen() {
        LinearLayout page = page();
        page.addView(bigHeader(kannada ? kn("Feedback") : "Feedback",
                kannada ? kn("Send suggestions") : "Send suggestions"));

        LinearLayout form = borderedBox();
        CheckBox anonymous = new CheckBox(this);
        anonymous.setText(kannada ? kn("Submit anonymously") : "Submit anonymously");
        anonymous.setTextColor(INK);
        anonymous.setChecked(true);
        form.addView(anonymous);

        EditText name = input(kannada ? kn("Your Name (Optional)") : "Your Name (Optional)");
        form.addView(name);

        EditText message = input(kannada ? kn("Write your suggestion here...") : "Write your suggestion here...");
        message.setMinLines(4);
        form.addView(message);

        Button submit = button(kannada ? kn("Submit") : "Submit", true);
        submit.setOnClickListener(v -> {
            if (TextUtils.isEmpty(message.getText().toString().trim())) {
                message.setError("Write your suggestion");
                return;
            }
            name.setText("");
            message.setText("");
            Toast.makeText(this, "Confirmed: your contribution has been logged.", Toast.LENGTH_LONG).show();
        });
        form.addView(submit);
        page.addView(form);

        page.addView(newsCard(kannada ? kn("Transparency") : "Transparency",
                kannada ? kn("Honest feedback helps us build a better future for students.") : "Honest feedback helps us build a better future for students.",
                kannada ? kn("Suggestions are reviewed by the school committee.") : "Suggestions are reviewed by the school committee."));
        return wrap(page);
    }

    private LinearLayout page() {
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(16), dp(18), dp(16), dp(28));
        return page;
    }

    private ScrollView wrap(View child) {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(PAPER);
        scroll.addView(child);
        return scroll;
    }

    private View bigHeader(String headline, String subtitle) {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(0, 0, 0, dp(18));
        header.addView(title(headline, 42));
        header.addView(small(subtitle.toUpperCase()));
        return header;
    }

    private View featureTile(String label, String detail, String tab) {
        LinearLayout tile = borderedBox();
        tile.setPadding(dp(14), dp(14), dp(14), dp(14));
        tile.addView(title(label, 16));
        tile.addView(small(detail));
        tile.setOnClickListener(v -> showTab(tab));
        return tile;
    }

    private View newsCard(String label, String headline, String body) {
        LinearLayout card = borderedBox();
        card.addView(colorBlock(150, ACCENT));
        card.addView(small(label.toUpperCase()));
        card.addView(title(headline, 22));
        card.addView(text(body, 14, false));
        return card;
    }

    private View newsCard(String label, String headline, String body, int imageRes) {
        LinearLayout card = borderedBox();
        card.addView(imageBlock(170, imageRes));
        card.addView(small(label.toUpperCase()));
        card.addView(title(headline, 22));
        card.addView(text(body, 14, false));
        return card;
    }

    private View mealCard(String date, String menu) {
        LinearLayout card = borderedBox();
        card.addView(small(date.toUpperCase()));
        card.addView(imageBlock(170, R.drawable.midday_meal_hd));
        card.addView(title(menu, 26));
        card.addView(text("Strict hygiene standards maintained as per state guidelines.", 14, false));
        return card;
    }

    private View facilityCard(String name, String detail) {
        LinearLayout card = borderedBox();
        card.addView(colorBlock(180, Color.rgb(214, 225, 235)));
        card.addView(title(name, 22));
        card.addView(text(detail, 14, false));
        return card;
    }

    private View starCard(String name, String category, String achievement) {
        LinearLayout card = borderedBox();
        card.addView(small(category.toUpperCase()));
        card.addView(title(name, 24));
        card.addView(text(achievement, 15, false));
        card.addView(text("Pride of Our School", 13, true));
        return card;
    }

    private View miniCard(String text) {
        LinearLayout card = borderedBox();
        card.setPadding(dp(8), dp(8), dp(8), dp(8));
        card.addView(colorBlock(80, Color.rgb(220, 220, 220)));
        TextView label = small(text);
        label.setGravity(Gravity.CENTER);
        card.addView(label);
        return card;
    }

    private View colorBlock(int height, int color) {
        TextView block = new TextView(this);
        block.setBackgroundColor(color);
        block.setText("SCHOOL PHOTO");
        block.setTextColor(INK);
        block.setGravity(Gravity.CENTER);
        block.setTypeface(Typeface.DEFAULT_BOLD);
        block.setTextSize(12);
        block.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(height)
        ));
        return block;
    }

    private View imageBlock(int height, int imageRes) {
        ImageView image = new ImageView(this);
        image.setImageResource(imageRes);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setAdjustViewBounds(false);
        image.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(height)
        ));
        return image;
    }

    private View sectionHeading(String text) {
        TextView heading = title(text, 24);
        heading.setPadding(0, dp(22), 0, dp(8));
        return heading;
    }

    private TextView editorialLabel(String label) {
        TextView view = small(label.toUpperCase());
        view.setGravity(Gravity.CENTER);
        view.setPadding(0, 0, 0, dp(16));
        return view;
    }

    private LinearLayout borderedBox() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(16), dp(16), dp(16), dp(16));
        box.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(16));
        box.setLayoutParams(params);
        return box;
    }

    private TextView title(String text, int size) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(INK);
        view.setTextSize(size);
        view.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC));
        view.setPadding(0, dp(4), 0, dp(6));
        return view;
    }

    private TextView text(String text, int size, boolean bold) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(INK);
        view.setTextSize(size);
        view.setPadding(0, dp(4), 0, dp(8));
        if (bold) view.setTypeface(Typeface.DEFAULT_BOLD);
        return view;
    }

    private TextView small(String text) {
        TextView view = text(text, 11, true);
        view.setTextColor(Color.rgb(92, 92, 92));
        return view;
    }

    private EditText input(String hint) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setTextSize(15);
        input.setSingleLine(false);
        input.setMinLines(2);
        input.setPadding(dp(8), dp(8), dp(8), dp(8));
        return input;
    }

    private Button button(String text, boolean filled) {
        Button button = new Button(this);
        button.setText(text);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setTextColor(filled ? Color.WHITE : INK);
        button.setBackgroundColor(filled ? INK : Color.WHITE);
        return button;
    }

    private LinearLayout.LayoutParams weightBox() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(dp(4), 0, dp(4), 0);
        return params;
    }

    private LinearLayout.LayoutParams fixed(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    private String kn(String key) {
        switch (key) {
            case "Shale-Namma Hemme":
                return "\u0CB6\u0CBE\u0CB2\u0CC6-\u0CA8\u0CAE\u0CCD\u0CAE \u0CB9\u0CC6\u0CAE\u0CCD\u0CAE\u0CC6";
            case "Home":
                return "\u0CAE\u0CC1\u0C96\u0CAA\u0CC1\u0C9F";
            case "Meals":
            case "Daily Meals":
                return "\u0CA6\u0CC8\u0CA8\u0C82\u0CA6\u0CBF\u0CA8 \u0C8A\u0C9F";
            case "Tour":
            case "Facility Tour":
            case "Open Facility Tour":
            case "Quick Facility Tour":
                return "\u0CB8\u0CCC\u0CB2\u0CAD\u0CCD\u0CAF\u0C97\u0CB3 \u0CAA\u0CCD\u0CB0\u0CB5\u0CBE\u0CB8";
            case "Stars":
            case "Student Stars":
                return "\u0CB5\u0CBF\u0CA6\u0CCD\u0CAF\u0CBE\u0CB0\u0CCD\u0CA5\u0CBF \u0CA4\u0CBE\u0CB0\u0CC6\u0C97\u0CB3\u0CC1";
            case "Feedback":
                return "\u0C85\u0CAD\u0CBF\u0CAA\u0CCD\u0CB0\u0CBE\u0CAF";
            case "Back":
                return "\u0CB9\u0CBF\u0C82\u0CA6\u0CC6";
            case "Shale-Namma":
                return "\u0CB6\u0CBE\u0CB2\u0CC6-\u0CA8\u0CAE\u0CCD\u0CAE";
            case "Modern School Portal":
                return "\u0C86\u0CA7\u0CC1\u0CA8\u0CBF\u0C95 \u0CB6\u0CBE\u0CB2\u0CBE \u0CAA\u0CCB\u0CB0\u0CCD\u0C9F\u0CB2\u0CCD";
            case "Transparency creates trust in our school community.":
                return "\u0CAA\u0CBE\u0CB0\u0CA6\u0CB0\u0CCD\u0CB6\u0C95\u0CA4\u0CC6\u0CAF\u0CC1 \u0CA8\u0CAE\u0CCD\u0CAE \u0CB6\u0CBE\u0CB2\u0CBE \u0CB8\u0CAE\u0CC1\u0CA6\u0CBE\u0CAF\u0CA6\u0CB2\u0CCD\u0CB2\u0CBF \u0CB5\u0CBF\u0CB6\u0CCD\u0CB5\u0CBE\u0CB8\u0CB5\u0CA8\u0CCD\u0CA8\u0CC1 \u0C89\u0C82\u0C9F\u0CC1\u0CAE\u0CBE\u0CA1\u0CC1\u0CA4\u0CCD\u0CA4\u0CA6\u0CC6.";
            case "Bulletin / 2h ago":
                return "\u0CB8\u0CC1\u0CA6\u0CCD\u0CA6\u0CBF / 2 \u0C97\u0C82\u0C9F\u0CC6 \u0CB9\u0CBF\u0C82\u0CA6\u0CC6";
            case "Special Report":
                return "\u0CB5\u0CBF\u0CB6\u0CC7\u0CB7 \u0CB5\u0CB0\u0CA6\u0CBF";
            case "Today's Mid-day Meal":
                return "\u0C87\u0C82\u0CA6\u0CBF\u0CA8 \u0CAE\u0CA7\u0CCD\u0CAF\u0CBE\u0CB9\u0CCD\u0CA8\u0CA6 \u0CAC\u0CBF\u0CB8\u0CBF\u0CAF\u0CC2\u0C9F";
            case "Daily Highlight":
                return "\u0CA6\u0CBF\u0CA8\u0CA6 \u0CAE\u0CC1\u0C96\u0CCD\u0CAF \u0CB8\u0CC1\u0CA6\u0CCD\u0CA6\u0CBF";
            case "Nutrition Report: Rice, Sambar and Protein Egg":
                return "\u0CAA\u0CCC\u0CB7\u0CCD\u0C9F\u0CBF\u0C95\u0CBE\u0C82\u0CB6 \u0CB5\u0CB0\u0CA6\u0CBF: \u0C85\u0CA8\u0CCD\u0CA8, \u0CB8\u0CBE\u0C82\u0CAC\u0CBE\u0CB0\u0CCD \u0CAE\u0CA4\u0CCD\u0CA4\u0CC1 \u0CAE\u0CCA\u0C9F\u0CCD\u0C9F\u0CC6";
            case "Quality verified meal served with care.":
                return "\u0C97\u0CC1\u0CA3\u0CAE\u0C9F\u0CCD\u0C9F \u0CAA\u0CB0\u0CBF\u0CB6\u0CC0\u0CB2\u0CBF\u0CA4 \u0C8A\u0C9F\u0CB5\u0CA8\u0CCD\u0CA8\u0CC1 \u0C95\u0CBE\u0CB3\u0C9C\u0CBF\u0CAF\u0CBF\u0C82\u0CA6 \u0CA8\u0CC0\u0CA1\u0CB2\u0CBE\u0CAF\u0CBF\u0CA4\u0CC1.";
            case "Science Lab":
                return "\u0CB5\u0CBF\u0C9C\u0CCD\u0C9E\u0CBE\u0CA8 \u0CAA\u0CCD\u0CB0\u0CAF\u0CCB\u0C97\u0CBE\u0CB2\u0CAF";
            case "Library":
                return "\u0C97\u0CCD\u0CB0\u0C82\u0CA5\u0CBE\u0CB2\u0CAF";
            case "Sports Room":
                return "\u0C95\u0CCD\u0CB0\u0CC0\u0CA1\u0CBE \u0C95\u0CCB\u0CA3\u0CC6";
            case "A nutritious meal served with care.":
                return "\u0C86\u0CB0\u0CCB\u0C97\u0CCD\u0CAF\u0C95\u0CB0 \u0C8A\u0C9F\u0CB5\u0CA8\u0CCD\u0CA8\u0CC1 \u0C95\u0CBE\u0CB3\u0C9C\u0CBF\u0CAF\u0CBF\u0C82\u0CA6 \u0CA8\u0CC0\u0CA1\u0CB2\u0CBE\u0C97\u0CC1\u0CA4\u0CCD\u0CA4\u0CA6\u0CC6.";
            case "Today":
                return "\u0C87\u0C82\u0CA6\u0CC1";
            case "Yesterday":
                return "\u0CA8\u0CBF\u0CA8\u0CCD\u0CA8\u0CC6";
            case "Rice, Sambar, Egg, Milk":
                return "\u0C85\u0CA8\u0CCD\u0CA8, \u0CB8\u0CBE\u0C82\u0CAC\u0CBE\u0CB0\u0CCD, \u0CAE\u0CCA\u0C9F\u0CCD\u0C9F\u0CC6, \u0CB9\u0CBE\u0CB2\u0CC1";
            case "Vegetable Pulav with Raita":
                return "\u0CA4\u0CB0\u0C95\u0CBE\u0CB0\u0CBF \u0CAA\u0CC1\u0CB2\u0CBE\u0CB5\u0CCD \u0CAE\u0CA4\u0CCD\u0CA4\u0CC1 \u0CB0\u0CBE\u0CAF\u0CBF\u0CA4\u0CBE";
            case "Dal Khichdi and Curd":
                return "\u0CA6\u0CBE\u0CB2\u0CCD \u0C96\u0CBF\u0C9A\u0CA1\u0CBF \u0CAE\u0CA4\u0CCD\u0CA4\u0CC1 \u0CAE\u0CCA\u0CB8\u0CB0\u0CC1";
            case "School facility gallery":
                return "\u0CB6\u0CBE\u0CB2\u0CBE \u0CB8\u0CCC\u0CB2\u0CAD\u0CCD\u0CAF\u0C97\u0CB3 \u0C97\u0CCD\u0CAF\u0CBE\u0CB2\u0CB0\u0CBF";
            case "Smart Classroom":
                return "\u0CB8\u0CCD\u0CAE\u0CBE\u0CB0\u0CCD\u0C9F\u0CCD \u0CA4\u0CB0\u0C97\u0CA4\u0CBF";
            case "Equipped with digital boards and educational software.":
                return "\u0CA1\u0CBF\u0C9C\u0CBF\u0C9F\u0CB2\u0CCD \u0CAC\u0CCB\u0CB0\u0CCD\u0CA1\u0CCD \u0CAE\u0CA4\u0CCD\u0CA4\u0CC1 \u0CB6\u0CC8\u0C95\u0CCD\u0CB7\u0CA3\u0CBF\u0C95 \u0CB8\u0CBE\u0CAB\u0CCD\u0C9F\u0CCD\u0CB5\u0CC7\u0CB0\u0CCD \u0C87\u0CB5\u0CC6.";
            case "Over 5,000 books available for students and community.":
                return "\u0CB5\u0CBF\u0CA6\u0CCD\u0CAF\u0CBE\u0CB0\u0CCD\u0CA5\u0CBF\u0C97\u0CB3\u0CC1 \u0CAE\u0CA4\u0CCD\u0CA4\u0CC1 \u0CB8\u0CAE\u0CC1\u0CA6\u0CBE\u0CAF\u0C95\u0CCD\u0C95\u0CC6 5,000 \u0CAA\u0CC1\u0CB8\u0CCD\u0CA4\u0C95\u0C97\u0CB3\u0CC1 \u0CB2\u0CAD\u0CCD\u0CAF.";
            case "Hygiene Toilets":
                return "\u0CB8\u0CCD\u0CB5\u0C9A\u0CCD\u0C9B \u0CB6\u0CCC\u0C9A\u0CBE\u0CB2\u0CAF\u0C97\u0CB3\u0CC1";
            case "Clean facilities with running water.":
                return "\u0CA8\u0CC0\u0CB0\u0CBF\u0CA8 \u0CB8\u0CCC\u0CB2\u0CAD\u0CCD\u0CAF\u0CA6\u0CCA\u0C82\u0CA6\u0CBF\u0C97\u0CC6 \u0CB8\u0CCD\u0CB5\u0C9A\u0CCD\u0C9B \u0CB8\u0CCC\u0CB2\u0CAD\u0CCD\u0CAF\u0C97\u0CB3\u0CC1.";
            case "Modern equipment for hands-on experiments.":
                return "\u0CAA\u0CCD\u0CB0\u0CBE\u0CAF\u0CCB\u0C97\u0CBF\u0C95 \u0CAA\u0CCD\u0CB0\u0CAF\u0CCB\u0C97\u0C97\u0CB3\u0CBF\u0C97\u0CC6 \u0C86\u0CA7\u0CC1\u0CA8\u0CBF\u0C95 \u0C89\u0CAA\u0C95\u0CB0\u0CA3\u0C97\u0CB3\u0CC1.";
            case "All student stars":
                return "\u0C8E\u0CB2\u0CCD\u0CB2\u0CBE \u0CB5\u0CBF\u0CA6\u0CCD\u0CAF\u0CBE\u0CB0\u0CCD\u0CA5\u0CBF \u0CA4\u0CBE\u0CB0\u0CC6\u0C97\u0CB3\u0CC1";
            case "Rahul Patil":
                return "\u0CB0\u0CBE\u0CB9\u0CC1\u0CB2\u0CCD \u0CAA\u0CBE\u0C9F\u0CC0\u0CB2\u0CCD";
            case "Priya Hegde":
                return "\u0CAA\u0CCD\u0CB0\u0CBF\u0CAF\u0CBE \u0CB9\u0CC6\u0C97\u0CCD\u0CA1\u0CC6";
            case "Student of the Week":
                return "\u0CB5\u0CBE\u0CB0\u0CA6 \u0CB5\u0CBF\u0CA6\u0CCD\u0CAF\u0CBE\u0CB0\u0CCD\u0CA5\u0CBF";
            case "Sports Winner":
                return "\u0C95\u0CCD\u0CB0\u0CC0\u0CA1\u0CBE \u0CB5\u0CBF\u0C9C\u0CC7\u0CA4";
            case "100% Attendance for 3 months":
                return "3 \u0CA4\u0CBF\u0C82\u0C97\u0CB3\u0CC1 100% \u0CB9\u0CBE\u0C9C\u0CB0\u0CBE\u0CA4\u0CBF";
            case "1st Prize in State Level Running":
                return "\u0CB0\u0CBE\u0C9C\u0CCD\u0CAF \u0CAE\u0C9F\u0CCD\u0C9F\u0CA6 \u0C93\u0C9F\u0CA6\u0CB2\u0CCD\u0CB2\u0CBF \u0CAA\u0CCD\u0CB0\u0CA5\u0CAE \u0CAC\u0CB9\u0CC1\u0CAE\u0CBE\u0CA8";
            case "Send suggestions":
                return "\u0CB8\u0CB2\u0CB9\u0CC6\u0C97\u0CB3\u0CA8\u0CCD\u0CA8\u0CC1 \u0C95\u0CB3\u0CC1\u0CB9\u0CBF\u0CB8\u0CBF";
            case "Submit anonymously":
                return "\u0CB9\u0CC6\u0CB8\u0CB0\u0CBF\u0CB2\u0CCD\u0CB2\u0CA6\u0CC6 \u0CB8\u0CB2\u0CCD\u0CB2\u0CBF\u0CB8\u0CBF";
            case "Your Name (Optional)":
                return "\u0CA8\u0CBF\u0CAE\u0CCD\u0CAE \u0CB9\u0CC6\u0CB8\u0CB0\u0CC1 (\u0C90\u0C9A\u0CCD\u0C9B\u0CBF\u0C95)";
            case "Write your suggestion here...":
                return "\u0CA8\u0CBF\u0CAE\u0CCD\u0CAE \u0CB8\u0CB2\u0CB9\u0CC6\u0CAF\u0CA8\u0CCD\u0CA8\u0CC1 \u0C87\u0CB2\u0CCD\u0CB2\u0CBF \u0CAC\u0CB0\u0CC6\u0CAF\u0CBF\u0CB0\u0CBF...";
            case "Submit":
                return "\u0CB8\u0CB2\u0CCD\u0CB2\u0CBF\u0CB8\u0CBF";
            case "Transparency":
                return "\u0CAA\u0CBE\u0CB0\u0CA6\u0CB0\u0CCD\u0CB6\u0C95\u0CA4\u0CC6";
            case "Honest feedback helps us build a better future for students.":
                return "\u0CAA\u0CCD\u0CB0\u0CBE\u0CAE\u0CBE\u0CA3\u0CBF\u0C95 \u0C85\u0CAD\u0CBF\u0CAA\u0CCD\u0CB0\u0CBE\u0CAF\u0CB5\u0CC1 \u0CB5\u0CBF\u0CA6\u0CCD\u0CAF\u0CBE\u0CB0\u0CCD\u0CA5\u0CBF\u0C97\u0CB3\u0CBF\u0C97\u0CC6 \u0C89\u0CA4\u0CCD\u0CA4\u0CAE \u0CAD\u0CB5\u0CBF\u0CB7\u0CCD\u0CAF \u0CA8\u0CBF\u0CB0\u0CCD\u0CAE\u0CBF\u0CB8\u0CB2\u0CC1 \u0CB8\u0CB9\u0CBE\u0CAF \u0CAE\u0CBE\u0CA1\u0CC1\u0CA4\u0CCD\u0CA4\u0CA6\u0CC6.";
            case "Suggestions are reviewed by the school committee.":
                return "\u0CB8\u0CB2\u0CB9\u0CC6\u0C97\u0CB3\u0CA8\u0CCD\u0CA8\u0CC1 \u0CB6\u0CBE\u0CB2\u0CBE \u0CB8\u0CAE\u0CBF\u0CA4\u0CBF \u0CAA\u0CB0\u0CBF\u0CB6\u0CC0\u0CB2\u0CBF\u0CB8\u0CC1\u0CA4\u0CCD\u0CA4\u0CA6\u0CC6.";
            default:
                return key;
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
