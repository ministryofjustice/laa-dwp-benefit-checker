{{/*
  Define environment variables that can be "included" in deployment.yaml
*/}}

{{/*
  Define Sentry environment variables
*/}}
{{- define "sentryConfig" }}
{{- if .Values.sentry.enabled }}
- name: SENTRY_ENABLED
  value: "true"
- name: SENTRY_DSN
  valueFrom:
    secretKeyRef:
      name: aws-secrets
      key: SENTRY_DSN
- name: SENTRY_ENVIRONMENT
  value: {{ .Values.sentry.environment | quote }}
- name: SENTRY_TRACES_SAMPLE_RATE
  value: {{ .Values.sentry.tracesSampleRate | quote }}
{{- else }}
- name: SENTRY_ENABLED
  value: "false"
{{- end }}
{{- end }}

{{- define "laa-benefit-checker-interim.env-vars"}}
- name: DWP_SOAP_URL
  valueFrom:
    secretKeyRef:
      name: aws-secrets
      key: DWP_SOAP_URL
- name: CREDENTIALS_SERVICE_CONTEXT
  valueFrom:
    secretKeyRef:
      name: aws-secrets
      key: CREDENTIALS_SERVICE_CONTEXT
- name: CREDENTIALS_CLIENT_IDS
  valueFrom:
    secretKeyRef:
      name: aws-secrets
      key: CREDENTIALS_CLIENT_IDS
{{- end }}
