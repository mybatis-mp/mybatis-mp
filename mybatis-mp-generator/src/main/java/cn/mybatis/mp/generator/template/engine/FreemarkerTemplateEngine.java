package cn.mybatis.mp.generator.template.engine;

import cn.mybatis.mp.generator.template.ITemplateBuilder;
import freemarker.core.PlainTextOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModelException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FreemarkerTemplateEngine implements TemplateEngine {

    private final Configuration configuration;

    public FreemarkerTemplateEngine() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");

        configuration.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        configuration.setClassForTemplateLoading(FreemarkerTemplateEngine.class, "/");
        try {
            configuration.setSharedVariable("util", new EngineUtil());
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(ITemplateBuilder templateBuilder) {
        try {
            Template template = configuration.getTemplate(templateBuilder.templateFilePath() + ".ftl");

            File outputFile = new File(templateBuilder.targetFilePath());
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                template.process(templateBuilder.contextData(), new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            } catch (Exception e) {
                outputFile.delete();
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
