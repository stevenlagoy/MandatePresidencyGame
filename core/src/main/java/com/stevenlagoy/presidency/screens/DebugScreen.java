package com.stevenlagoy.presidency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.stevenlagoy.presidency.GameRoot;
import com.stevenlagoy.presidency.core.Engine;
import com.stevenlagoy.presidency.core.Manager;
import com.stevenlagoy.presidency.util.ScriptConsole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DebugScreen extends BaseScreen {

    private BitmapFont rowFont;
    private BitmapFont detailFont;

    private static final Color BASE       = new Color(0.75f, 0.75f, 0.75f, 1f);
    private static final Color HOVER      = new Color(0.55f, 0.55f, 0.55f, 1f);
    private static final Color DOWN       = new Color(0.90f, 0.90f, 0.90f, 1f);
    private static final Color SELECTED   = new Color(0.35f, 0.65f, 0.95f, 1f);
    private static final Color LIGHT_TEXT = new Color(0.9f, 0.9f, 0.9f, 1f);
    private static final Color DARK_TEXT  = new Color(0.2f, 0.2f, 0.2f, 1f);

    // Rows and selection tracking
    private final List<TextButton> rowButtons = new ArrayList<>();
    private final List<Manager> rowManagers = new ArrayList<>();
    private TextButton selectedButton = null;
    private Manager selectedManager = null;
    private TextField consoleInput;
    private Label consoleOutputLabel;
    private ScrollPane consoleOutputScroll;

    // Detail panel widgets, updated on selection / action
    private Label detailTitleLabel;
    private Label detailStateLabel;
    private Label detailSuperLabel;
    private Label detailTimeLabel;
    private Label statusLabel;

    public DebugScreen(GameRoot game) {
        super(game);
    }

    @Override
    protected void buildUI(@NotNull Table root, @NotNull Skin skin) {
        rowFont = new BitmapFont(Gdx.files.internal("ui/fonts/arial.fnt"));
        rowFont.getData().setScale(0.55f);

        detailFont = new BitmapFont(Gdx.files.internal("ui/fonts/arial.fnt"));
        detailFont.getData().setScale(0.55f);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = rowFont;
        buttonStyle.fontColor = DARK_TEXT;
        buttonStyle.up   = skin.getDrawable("buttons/button");
        buttonStyle.over = skin.getDrawable("buttons/button");
        buttonStyle.down = skin.getDrawable("buttons/button");

        root.pad(20);

        // Top bar: back button
        Table topBar = new Table();
        TextButton back = createActionButton("Back to Menu", buttonStyle);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenManager().show(MainMenuScreen.class, () -> new MainMenuScreen(game));
            }
        });
        topBar.add(back).height(40).width(160).left();
        root.add(topBar).growX().top().padBottom(10).row();

        // Main split: tree on the left, details on the right
        Table splitRow = new Table();
        root.add(splitRow).grow().row();

        // Left: manager tree
        Table treeContent = new Table();
        treeContent.top().left();
        treeContent.defaults().padTop(4).left();

        Manager rootManager = Engine.getInstance();
        if (rootManager != null) {
            buildManagerRows(rootManager, 0, treeContent, buttonStyle);
        }
        else {
            Label noEngine = new Label("No Engine instance available.", new Label.LabelStyle(detailFont, LIGHT_TEXT));
            treeContent.add(noEngine).row();
        }

        ScrollPane treeScroll = new ScrollPane(treeContent);
        treeScroll.setFadeScrollBars(false);
        treeScroll.setScrollingDisabled(true, false);

        Table treePanel = new Table();
        treePanel.setBackground(skin.getDrawable("panels/panel"));
        treePanel.setColor(1f, 1f, 1f, 0.6f);
        treePanel.pad(10);
        treePanel.add(treeScroll).grow();

        splitRow.add(treePanel).width(Gdx.graphics.getWidth() * 0.33f).growY().padRight(10);

        // Right: detail panel
        Table detailPanel = new Table();
        detailPanel.setBackground(skin.getDrawable("panels/panel"));
        detailPanel.setColor(1f, 1f, 1f, 0.6f);
        detailPanel.top().left();
        detailPanel.pad(20);
        detailPanel.defaults().left().padBottom(8);

        Label.LabelStyle titleStyle = new Label.LabelStyle(detailFont, LIGHT_TEXT);
        Label.LabelStyle plainStyle = new Label.LabelStyle(detailFont, LIGHT_TEXT);

        detailTitleLabel = new Label("No manager selected", titleStyle);
        detailStateLabel = new Label("", plainStyle);
        detailSuperLabel = new Label("", plainStyle);
        detailTimeLabel  = new Label("", plainStyle);
        statusLabel      = new Label("", new Label.LabelStyle(detailFont, Color.FIREBRICK));
        statusLabel.setWrap(true);

        detailPanel.add(detailTitleLabel).row();
        detailPanel.add(detailStateLabel).row();
        detailPanel.add(detailSuperLabel).row();
        detailPanel.add(detailTimeLabel).padBottom(16).row();

        Table actionRow = new Table();
        actionRow.defaults().padRight(10);
        TextButton initBtn     = createActionButton("Init", buttonStyle);
        TextButton pauseBtn    = createActionButton("Pause", buttonStyle);
        TextButton unpauseBtn  = createActionButton("Unpause", buttonStyle);
        TextButton cleanupBtn  = createActionButton("Cleanup", buttonStyle);
        TextButton refreshBtn  = createActionButton("Refresh", buttonStyle);

        initBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runManagerAction(Manager::init);
            }
        });
        pauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runManagerAction(Manager::pause);
            }
        });
        unpauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runManagerAction(Manager::unpause);
            }
        });
        cleanupBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runManagerAction(Manager::cleanup);
            }
        });
        refreshBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                statusLabel.setText("");
                updateDetailPanel(selectedManager);
            }
        });

        actionRow.add(initBtn).height(36).width(90);
        actionRow.add(pauseBtn).height(36).width(90);
        actionRow.add(unpauseBtn).height(36).width(100);
        actionRow.add(cleanupBtn).height(36).width(100);
        actionRow.add(refreshBtn).height(36).width(90);

        detailPanel.add(actionRow).padBottom(16).row();
        detailPanel.add(statusLabel).growX().row();

        splitRow.add(detailPanel).grow();

        updateDetailPanel(null);

        // Bottom bar: single-statement evaluator
        Table consoleBar = new Table();
        consoleBar.pad(10);

        TextField.TextFieldStyle fieldStyle = new TextField.TextFieldStyle();
        fieldStyle.font = detailFont;
        fieldStyle.fontColor = LIGHT_TEXT;
        fieldStyle.background = skin.getDrawable("panels/panel"); // reusing existing panel drawable

        consoleInput = new TextField("", fieldStyle);
        consoleInput.setMessageText("Enter a statement, e.g. Engine.getInstance().getGameDifficulty()");
        consoleInput.setTextFieldListener((textField, c) -> {
            if (c == '\n' || c == '\r') runEval();
        });

        TextButton evalButton = createActionButton("Evaluate", buttonStyle);
        evalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runEval();
            }
        });

        Label.LabelStyle outputStyle = new Label.LabelStyle(detailFont, LIGHT_TEXT);
        consoleOutputLabel = new Label("", outputStyle);
        consoleOutputLabel.setWrap(true);

        Table consoleOutputContainer = new Table();
        consoleOutputContainer.top().left();
        consoleOutputContainer.add(consoleOutputLabel).growX().top().left();

        consoleOutputScroll = new ScrollPane(consoleOutputContainer);
        consoleOutputScroll.setFadeScrollBars(false);
        consoleOutputScroll.setScrollingDisabled(true, false);

        consoleBar.add(consoleOutputScroll).growX().height(80).colspan(2).row();
        consoleBar.add(consoleInput).growX().height(36).padTop(8);
        consoleBar.add(evalButton).width(80).height(36).padLeft(10).padTop(8);

        root.add(consoleBar).growX().padTop(10).row();
    }

    private void runEval() {
        String statement = consoleInput.getText();
        if (statement == null || statement.isBlank()) return;
        String result = ScriptConsole.evaluate(statement);
        consoleOutputLabel.setText(consoleOutputLabel.getText() + "\n> " + statement + "\n" + result);
        consoleInput.setText("");
        consoleOutputScroll.layout();
        consoleOutputScroll.setScrollPercentY(1f);
    }

    /**
     * Recursively add a row for the given manager and all of its submanagers, indenting
     * each level to visually represent the tree.
     */
    private void buildManagerRows(@NotNull Manager manager, int depth, @NotNull Table container, TextButton.TextButtonStyle style) {
        String indent = "   ".repeat(depth);
        TextButton row = new TextButton(indent + manager.getClass().getSimpleName() + "  [" + manager.getState() + "]", style);
        row.getLabel().setColor(LIGHT_TEXT);
        row.setColor(BASE);

        rowButtons.add(row);
        rowManagers.add(manager);

        row.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectManager(manager, row);
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                if (row != selectedButton) row.setColor(HOVER);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                if (row != selectedButton) row.setColor(BASE);
            }
        });

        container.add(row).growX().height(32).row();

        for (Manager sub : manager.getSubManagers()) {
            buildManagerRows(sub, depth + 1, container, style);
        }
    }

    private void selectManager(@NotNull Manager manager, @NotNull TextButton row) {
        if (selectedButton != null) selectedButton.setColor(BASE);
        selectedButton = row;
        selectedManager = manager;
        row.setColor(SELECTED);
        statusLabel.setText("");
        updateDetailPanel(manager);
    }

    private void updateDetailPanel(@Nullable Manager manager) {
        if (manager == null) {
            detailTitleLabel.setText("No manager selected");
            detailStateLabel.setText("");
            detailSuperLabel.setText("");
            detailTimeLabel.setText("");
            return;
        }
        detailTitleLabel.setText(manager.getClass().getSimpleName());
        detailStateLabel.setText("State: " + manager.getState());
        detailSuperLabel.setText("Super manager: " +
            (manager.superManager == null ? "(root)" : manager.superManager.getClass().getSimpleName()));
        Engine engine = Engine.getInstance();
        if (engine != null) {
            detailTimeLabel.setText(String.format("Program time: %.2fs", engine.getProgramTime()));
        }

        // Refresh the row label so the state shown in the tree stays in sync
        int idx = rowManagers.indexOf(manager);
        if (idx >= 0) {
            TextButton row = rowButtons.get(idx);
            String indent = row.getText().toString();
            int bracket = indent.indexOf(manager.getClass().getSimpleName());
            if (bracket >= 0) {
                String prefix = indent.substring(0, bracket);
                row.setText(prefix + manager.getClass().getSimpleName() + "  [" + manager.getState() + "]");
            }
        }
    }

    /**
     * Run a lifecycle action against the currently selected manager, catching any
     * IllegalStateException so an invalid transition surfaces in the UI instead of crashing.
     */
    private void runManagerAction(@NotNull java.util.function.Consumer<Manager> action) {
        if (selectedManager == null) {
            statusLabel.setText("No manager selected.");
            return;
        }
        try {
            action.accept(selectedManager);
            statusLabel.setColor(Color.FOREST);
            statusLabel.setText("OK.");
        }
        catch (IllegalStateException e) {
            statusLabel.setColor(Color.FIREBRICK);
            statusLabel.setText(e.getMessage());
        }
        updateDetailPanel(selectedManager);
    }

    private TextButton createActionButton(String text, TextButton.TextButtonStyle style) {
        TextButton btn = new TextButton(text, style);
        btn.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                if (btn != selectedButton) btn.setColor(HOVER);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                if (btn != selectedButton) btn.setColor(BASE);
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btn.setColor(DOWN);
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btn.setColor(HOVER);
            }
        });
        btn.setColor(BASE);
        btn.getLabel().setColor(LIGHT_TEXT);
        return btn;
    }

    @Override
    public void dispose() {
        super.dispose();
        rowFont.dispose();
        detailFont.dispose();
    }
}
