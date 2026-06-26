package com.agendas.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DiagnosticCatalog {

    private final List<QuestionDefinition> questions = List.of(
            new QuestionDefinition(1, "Política de datos personales", "Art. 12 y Art. 17(k)", 0, null, null,
                    "¿Cuenta con una política de tratamiento de datos personales?",
                    "Manual interno, política publicada, documento aprobado o URL pública.",
                    "Crear y aprobar una política de tratamiento con responsable, finalidades, derechos, canales, tiempos de respuesta y medidas de seguridad."),
            new QuestionDefinition(2, "Política de datos personales", "Art. 11 y Art. 17(k)", 10, 1, null,
                    "¿La política está documentada y publicada en medio de fácil acceso?",
                    "URL pública, intranet accesible, documento entregable o aviso de privacidad visible.",
                    "Publicar la política en un canal estable y fácil de consultar, con versión, fecha de actualización y datos de contacto."),
            new QuestionDefinition(3, "Política de datos personales", "Art. 4(b) y Art. 12(a)", 10, 1, null,
                    "¿Define las finalidades del tratamiento de datos?",
                    "Inventario de finalidades por formulario, proceso, base de datos o sistema.",
                    "Mapear datos contra finalidades específicas y eliminar usos genéricos sin explicación concreta."),
            new QuestionDefinition(4, "Política de datos personales", "Art. 8 y Art. 12(c)", 10, 1, null,
                    "¿Incluye los derechos de los titulares?",
                    "Sección de derechos del titular dentro de la política.",
                    "Agregar una sección clara con derechos de titulares, acceso, actualización, rectificación, supresión y revocatoria."),
            new QuestionDefinition(5, "Política de datos personales", "Art. 8, Art. 14 y Art. 15", 10, 1, null,
                    "¿Menciona cómo ejercer los derechos de los titulares?",
                    "Correo, formulario, responsable, tiempos de respuesta y procedimiento documentado.",
                    "Definir canales, responsable, plazos, requisitos mínimos y trazabilidad para consultas y reclamos."),
            new QuestionDefinition(6, "Privacidad desde el diseño", "Art. 17(d)", 12, null, null,
                    "¿Incorpora evaluaciones de impacto en privacidad?",
                    "Plantilla PIA/DPIA, matriz de riesgos, actas de revisión o aprobación previa al despliegue.",
                    "Adoptar una evaluación de impacto para proyectos que recolecten datos personales, con riesgos, controles y responsables."),
            new QuestionDefinition(7, "Privacidad desde el diseño", "Art. 4(b) y Art. 4(d)", 12, null, null,
                    "¿Aplica técnicas de minimización de datos?",
                    "Inventario de campos, justificación por dato, formularios reducidos o reglas de retención.",
                    "Revisar formularios y bases de datos para retirar campos innecesarios, opcionales no justificados y duplicados."),
            new QuestionDefinition(8, "Privacidad desde el diseño", "Art. 4(b), Art. 4(f) y Art. 17(d)", 12, null, null,
                    "¿Configura sus sistemas para recopilar el mínimo de datos por defecto?",
                    "Configuraciones por defecto, revisiones de formularios, controles de permisos o políticas de retención.",
                    "Aplicar privacidad por defecto: campos mínimos, permisos limitados, consentimiento explícito y retención reducida."),
            new QuestionDefinition(9, "Gobernanza", "Art. 17(d) y Art. 17(n)", 16, null, null,
                    "¿Cuenta con un sistema de administración de riesgos?",
                    "Matriz de riesgos, inventario de activos, plan de tratamiento, controles y respuesta a incidentes.",
                    "Implementar un sistema mínimo de riesgos con inventario, amenazas, controles, responsables, revisión periódica y plan de incidentes."),
            new QuestionDefinition(10, "Gobernanza", "Art. 17(k)", 8, null, null,
                    "¿Cuenta con un oficial de protección de datos personales?",
                    "Rol asignado, funciones, correo de contacto, acta o nombramiento interno.",
                    "Asignar un responsable de protección de datos con funciones, autoridad, canal de contacto y revisión periódica de cumplimiento."),
            new QuestionDefinition(11, "Gobernanza", "Art. 17(k)", 0, null, 10,
                    "¿El oficial de protección de datos está designado formalmente?",
                    "Acta, resolución interna, contrato, manual de funciones o comunicación oficial.",
                    "Formalizar el nombramiento con alcance, responsabilidades, suplencia, independencia operativa y canal de atención.")
    );

    private final Map<Integer, QuestionDefinition> byNumber = questions.stream()
            .collect(Collectors.toUnmodifiableMap(QuestionDefinition::number, Function.identity()));

    public List<QuestionDefinition> questions() {
        return questions;
    }

    public QuestionDefinition get(int number) {
        QuestionDefinition definition = byNumber.get(number);
        if (definition == null) {
            throw new IllegalArgumentException("Pregunta no soportada: " + number);
        }
        return definition;
    }
}
