/**
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

const TextField = ({
  className,
  helpText,
  label,
  onChange,
  parameterName,
  parameterValue,
  required,
}) => {
  const { t } = useTranslation();

  return (
    <div className={'form-group ' + (className ? className : '')}>
      <label htmlFor={parameterName}>{label ? label : t(parameterName + '-label')}</label>
      <input
        className="form-control"
        id={parameterName}
        onChange={(e) => {
          onChange(parameterName, e.currentTarget.value);
        }}
        placeholder={t(parameterName + '-placeholder')}
        required={required ? 'required' : ''}
        type="text"
        value={parameterValue}
      />
      <small className="form-text text-muted">
        {helpText ? helpText : t(parameterName + '-help')}
      </small>
    </div>
  );
};

export default TextField;
