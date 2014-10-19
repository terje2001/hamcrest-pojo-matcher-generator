package ru.yandex.qatools.processors.matcher.gen.processing;

import ch.lambdaj.function.convert.Converter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import ru.yandex.qatools.processors.matcher.gen.util.GenUtils;
import ru.yandex.qatools.processors.matcher.gen.bean.ClassDescription;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Joiner.on;
import static com.google.common.io.CharStreams.asWriter;
import static java.lang.String.format;

/**
 * User: lanwen
 * Date: 18.09.14
 * Time: 18:34
 */
public class ClassDescriptionProcessing implements Converter<ClassDescription, Void> {

    public static final String CLASS_TEMPLATE = "templates/class.vm";

    public static Logger LOGGER = Logger.getLogger(ClassDescriptionProcessing.class.toString());


    private Filer filer;
    private VelocityEngine engine;

    private ClassDescriptionProcessing(Filer filer, VelocityEngine engine) {
        this.filer = filer;
        this.engine = engine;
    }

    public static ClassDescriptionProcessing processClassDescriptionsWith(Filer filer, VelocityEngine engine) {
        return new ClassDescriptionProcessing(filer, engine);
    }


    @Override
    public Void convert(ClassDescription from) {
        VelocityContext context = new VelocityContext();

        context.put("type", from);
        context.put("utils", new GenUtils());

        try {
            JavaFileObject jfo = filer
                    .createSourceFile(new GenUtils().withGeneratedSuffix(on(".")
                            .join(from.packageName(), from.name())));

            try (Writer writer = jfo.openWriter()) {
                engine.getTemplate(CLASS_TEMPLATE).merge(context, writer);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, format("Error: %s", e.getMessage()), e);
        }

        return null;
    }
}
