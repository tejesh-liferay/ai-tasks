/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { memo } from 'react';

import BaseNode from './BaseNode';

const WebhookNode = memo(({ data, id, type }) => {
  const { label, parameters } = data;
  const details = [['url', parameters.url]];

  return <BaseNode className="utility-node" details={details} id={id} label={label} type={type} />;
});

export default WebhookNode;
