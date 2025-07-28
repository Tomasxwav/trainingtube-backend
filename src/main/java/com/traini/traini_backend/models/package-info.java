/**
 * Paquete que contiene las entidades del modelo de datos.
 * Aquí se definen los filtros globales de Hibernate.
 */
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "companyId", type = Long.class))
package com.traini.traini_backend.models;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
