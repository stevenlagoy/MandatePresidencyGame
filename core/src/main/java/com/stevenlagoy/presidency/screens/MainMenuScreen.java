package com.stevenlagoy.presidency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.stevenlagoy.presidency.GameRoot;

public class MainMenuScreen extends BaseScreen {

    private Texture background;
    private Texture logoTexture;
    private BitmapFont buttonFont;

    private static final Color BASE  = new Color(0.75f, 0.75f, 0.75f, 1f);
    private static final Color HOVER = new Color(0.55f, 0.55f, 0.55f, 1f);
    private static final Color DOWN  = new Color(0.90f, 0.90f, 0.90f, 1f);
    private static final Color TEXT  = new Color(0.15f, 0.15f, 0.15f, 1f);

    public MainMenuScreen(GameRoot game) {
        super(game);
    }

    @Override
    protected void buildUI(Table root, Skin skin) {
        background = new Texture("textures/backgrounds/background.png");
        logoTexture = new Texture("textures/logos/game_logo.png");

        Image logo = new Image(logoTexture);
        logo.setScaling(Scaling.fit);

        root.left();

        Table leftContainer = new Table();
        leftContainer.left().top();
        leftContainer.pad(40);
        leftContainer.defaults().pad(10);
        root.add(leftContainer).width(Gdx.graphics.getWidth() * 0.33f).expandY().fillY().left();

        leftContainer.add(logo).growX().expand().top().padBottom(30).row();

        Table panelTable = new Table();
        panelTable.setBackground(skin.getDrawable("panels/panel"));
        panelTable.setColor(1f, 1f, 1f, 0.6f);
        panelTable.pad(40);
        panelTable.defaults().pad(10);

        buttonFont = new BitmapFont(Gdx.files.internal("ui/fonts/arial.fnt"));
        buttonFont.getData().setScale(0.6f);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = TEXT;
        buttonStyle.up   = skin.getDrawable("buttons/button");
        buttonStyle.over = skin.getDrawable("buttons/button");
        buttonStyle.down = skin.getDrawable("buttons/button");

        TextButton newGame = createButton("New Game", buttonStyle);
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getEngine().init();
                game.getScreenManager().show(MapScreen.class, () -> new MapScreen(game));
            }
        });

        TextButton cont = createButton("Continue", buttonStyle);
        TextButton load = createButton("Load Game", buttonStyle);
        panelTable.add(newGame).growX().height(60).pad(40).row();
        panelTable.add(cont).growX().height(60).pad(40).row();
        panelTable.add(load).growX().height(60).pad(40).padBottom(20).row();

        Table iconRow = new Table();
        TextButton settings = createButton("Settings", buttonStyle);
        TextButton achievements = createButton("Achievements", buttonStyle);
        TextButton encyclopedia = createButton("Encyclopedia", buttonStyle);
        TextButton credits = createButton("Credits", buttonStyle);
        iconRow.defaults().pad(10);
        iconRow.add(settings).size(120);
        iconRow.add(achievements).size(120);
        iconRow.add(encyclopedia).size(120);
        iconRow.add(credits).size(120);
        panelTable.add(iconRow).padBottom(20).row();

        TextButton exit = createButton("Exit", buttonStyle);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        panelTable.add(exit).growX().height(60).pad(40).row();

        leftContainer.add(panelTable).growX().top().bottom().row();
    }

    private TextButton createButton(String text, TextButton.TextButtonStyle style) {
        TextButton btn = new TextButton(text, style);
        btn.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                btn.setColor(HOVER);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                btn.setColor(BASE);
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
        btn.getLabel().setColor(TEXT);
        return btn;
    }

    @Override
    protected void renderBackground(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        logoTexture.dispose();
        buttonFont.dispose();
    }
}
