<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.culturegraph.org/metamorph"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	>
		
	<xsl:template match="/">

<metamorph xmlns="http://www.culturegraph.org/metamorph"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.culturegraph.org/metamorph metamorph.xsd">

			<xsl:apply-templates />
		</metamorph>
	</xsl:template>

	<xsl:template match="IndexConfiguration/Catalog/Indices/Index">
		<group name="{@indexName}">
			<xsl:apply-templates />
		</group>
	</xsl:template>
	
	<xsl:template match="IndexConfiguration/Catalog/Indices/ComplexIndex">
		<xsl:variable name="indexName" select="@indexName" />
		<xsl:variable name="indexFieldValue" select="@indexFieldValue" />
		
		<xsl:for-each select="PicaFields/and">
				<collect-literal name="{$indexName}" value="{$indexFieldValue}">
			<xsl:for-each select="Field">
			<data source="{@fieldId}.{@subfieldId}">
				<regexp match="{@restriction}"></regexp>
			</data><xsl:text>&#10;</xsl:text>
			</xsl:for-each>
			</collect-literal>
			</xsl:for-each>
			
		
	</xsl:template>
	
	<xsl:template match="PicaFields/and">

	</xsl:template>

	<xsl:template match="PicaField">
		<xsl:variable name="field_id" select="@fieldId" />
		<xsl:for-each select="PicaSubfield">
			<xsl:choose>
				<xsl:when test="contains(string($field_id), ':') ">
					<data source="{substring-before($field_id,':')}.9">
						<compose postfix=".{substring-after(string($field_id), ':')}.{@subfieldId}"></compose>
						<lookup in="catalog"></lookup>
					</data>
				</xsl:when>
				<xsl:otherwise>
					<data name="{$field_id}.{@subfieldId}"></data>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>