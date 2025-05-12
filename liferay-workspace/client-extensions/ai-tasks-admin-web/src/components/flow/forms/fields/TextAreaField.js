/**
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

const TextAreaField = ({
  className,
  defaultValue,
  helpText,
  label,
  onChange,
  parameterName,
  parameterValue,
  required,
  rows,
}) => {
  const { t } = useTranslation();

  return (
    <div className={'form-group ' + (className ? className : '')}>
      <label htmlFor={parameterName}>{label ? label : t(parameterName + '-label')}</label>
      <textarea
        className="form-control"
        id={parameterName}
        placeholder={t(parameterName + '-placeholder')}
        rows={rows ? rows : 5}
        value={parameterValue || defaultValue}
        onChange={(e) => {
          onChange(parameterName, e.currentTarget.value);
        }}
        required={required ? 'required' : ''}
      />
      <small className="form-text text-muted">
        {helpText ? helpText : t(parameterName + '-help')}
      </small>
    </div>
  );
};

export default TextAreaField;
