package com.stevenlagoy.presidency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stevenlagoy.presidency.GameRoot;

public class MainMenuScreen implements Screen {

    private final GameRoot game;

    private Stage stage;
    private SpriteBatch batch;

    private Texture background;
    private Texture whiteTexture;

    Color base  = new Color(0.75f, 0.75f, 0.75f, 1f); // light gray
    Color hover = new Color(0.55f, 0.55f, 0.55f, 1f); // darker
    Color down  = new Color(0.90f, 0.90f, 0.90f, 1f); // lighter
    Color regularText = new Color(0.15f, 0.15f, 0.15f, 1f);

    public MainMenuScreen(GameRoot game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport());

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whiteTexture = new Texture(pixmap);
        pixmap.dispose();

        // Load background
        background = new Texture("textures/backgrounds/background.png");

        // Basic UI skin
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin();
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("uiskin.json"));

        // Create logo
        Image logo = new Image(new Texture("textures/logos/game_logo.png"));
        logo.setScaling(Scaling.fit);

        Table root = new Table();
        root.setFillParent(true);
        root.left();
        stage.addActor(root);

        Table leftContainer = new Table();
        leftContainer.left().top();
        leftContainer.pad(40);
        leftContainer.defaults().pad(10);
        root.add(leftContainer).width(Gdx.graphics.getWidth() * 0.33f).expandY().fillY().left(); // Force into left third

        leftContainer.add(logo).growX().expand().top().padBottom(30).row();

        Table panelTable = new Table();
        panelTable.setBackground(skin.getDrawable("panels/panel"));
        panelTable.setColor(1f, 1f, 1f, 0.6f);
        panelTable.pad(40);
        panelTable.defaults().pad(10);

        BitmapFont font = new BitmapFont(Gdx.files.internal("ui/fonts/arial.fnt"));
        font.getData().setScale(0.6f);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = regularText;

        buttonStyle.up   = skin.getDrawable("buttons/button");
        buttonStyle.over = skin.getDrawable("buttons/button");
        buttonStyle.down = skin.getDrawable("buttons/button");

        TextButton newGame = createButton("New Game", buttonStyle);
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
        panelTable.add(exit).growX().height(60).pad(40).row();

        leftContainer.add(panelTable).growX().top().bottom().row();

        Gdx.input.setInputProcessor(stage);
    }

    private TextButton createButton(String text, TextButton.TextButtonStyle style) {
        TextButton btn = new TextButton(text, style);

        btn.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                btn.setColor(hover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                btn.setColor(base);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btn.setColor(down);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btn.setColor(hover);
            }
        });

        btn.setColor(base);
        btn.getLabel().setColor(regularText);

        return btn;
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background first
        batch.begin();
        batch.draw(background, 0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
        batch.end();

        // Then draw UI
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        background.dispose();
        whiteTexture.dispose();
    }

}
