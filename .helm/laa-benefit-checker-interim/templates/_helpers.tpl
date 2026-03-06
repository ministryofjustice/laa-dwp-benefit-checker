{{/*
_helpers.tpl
This file contains Helm template helpers that can be reused throughout the chart.
*/}}

{{/*
Expand the name of the chart.
*/}}
{{- define "laa-benefit-checker-interim.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "laa-benefit-checker-interim.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "laa-benefit-checker-interim.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "laa-benefit-checker-interim.labels" -}}
helm.sh/chart: {{ include "laa-benefit-checker-interim.chart" . }}
{{ include "laa-benefit-checker-interim.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "laa-benefit-checker-interim.selectorLabels" -}}
app.kubernetes.io/name: {{ include "laa-benefit-checker-interim.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app: {{ template "laa-benefit-checker-interim.name" . }}
release: {{ .Release.Name }}
{{- end }}

{{/*
Create ingress configuration
*/}}
{{- define "laa-benefit-checker-interim.ingress" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace "aws-secrets") }}
{{- if $secret }}
{{- $allowlistSourceRange := $secret.data.ALLOWLIST_SOURCE_RANGE | b64dec }}
{{- if $allowlistSourceRange }}
nginx.ingress.kubernetes.io/whitelist-source-range: {{ $allowlistSourceRange }}
external-dns.alpha.kubernetes.io/set-identifier: {{ include "laa-benefit-checker-interim.fullname" . }}-{{ $.Values.ingress.environmentName}}-green
{{- end }}
{{- end }}
{{- end }}
